package com.example.luo_pc.news.model;

import android.content.Context;
import android.util.Log;

import com.example.luo_pc.news.bean.NewsBean;
import com.example.luo_pc.news.fragment.NewsListFragment2;
import com.example.luo_pc.news.utils.FileUtils;
import com.example.luo_pc.news.utils.JsonUtils;
import com.example.luo_pc.news.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/6.
 * <p>
 * 此类是为新闻列表界面的Model，获取数据和部分业务逻辑在此处理
 */
public class NewsListModelImpl implements NewsListModel<List<NewsBean>> {
    @SuppressWarnings("unused")
    //suppress unused because I think this variable should be here though it have never be used
    private static String TAG = "NewsListModelImpl";
    private final List<NewsBean> list = new ArrayList<>();
    private String value;
    private ObjectInputStream ois;
    private boolean writeFlag = true;
    //在读缓存的情况下，只允许读一次
    private boolean readFlag = true;

    //-----------------------interface's method----------------------------//
    @Override
    public void getNews(int pageIndex, final String type, final GetNewsStatus<List<NewsBean>> getNewsStatus, Context context) {
        getData(pageIndex, type, getNewsStatus, context);
    }

    //------------------------------my method------------------------------//

    /**
     * 拼接url
     */
    private String getUrl(int pageIndex, String type) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            //头条
            case NewsListFragment2.TOP_FRAGMENT:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                setType(Urls.TOP_ID);
                break;
            //NBA
            case NewsListFragment2.NBA_FRAGMENT:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                setType(Urls.NBA_ID);
                break;
            //汽车
            case NewsListFragment2.CAR_FRAGMENT:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                setType(Urls.CAR_ID);
                break;
            //笑话
            case NewsListFragment2.JOKE_FRAGMENT:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                setType(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                setType(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }

    /**
     * 获取正确的字符
     */
    private void setType(String value) {
        this.value = value;
    }

    /**
     * 先从网络获取数据，如果获取不到则从缓存中读取数据
     * <p>
     * 对于现在我这种rx的用法，恩，太糟了，又出现了迷之缩进
     * 待完善
     */
    private void getData(final int pageIndex, final String type, final GetNewsStatus<List<NewsBean>> getNewsStatus, final Context context) {
        Observable.just(getUrl(pageIndex, type)).map(new Func1<String, List<NewsBean>>() {
            @Override
            public List<NewsBean> call(String s) {
                OkHttpUtils.get()
                        .url(getUrl(pageIndex, type))
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        getNewsStatus.onFailed(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        List<NewsBean> newsList = JsonUtils.readJsonNewsBean(response, value);
                        if (writeFlag)
                            if (newsList != null)
                                for (NewsBean i : newsList) {
                                    NewsListModelImpl.this.list.add(i);
                                }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                saveToFile(context, newsList, "NewsBean" + value);
                            }
                        }).start();

                        getNewsStatus.onSuccess(newsList);
                    }
                });
                Log.e(TAG, "走了这？");
                return NewsListModelImpl.this.list;
            }
        }).subscribe(new Action1<List<NewsBean>>() {
            @Override
            public void call(List<NewsBean> newsBeen) {
                if (newsBeen.size() != 0)
                    return;
                List<NewsBean> newsListFromFile = getNewsListFromFile(context);
                if (newsListFromFile != null) {
                    getNewsStatus.onSuccess(newsListFromFile);
                    return;
                }
                getNewsStatus.onFailed();
            }
        });
    }

    /**
     * 只有第一次读会返回数据，其他的时候返回null
     */
    //suppress unchecked because I will deal with if it is a null list
    @SuppressWarnings("unchecked")
    private List<NewsBean> getNewsListFromFile(Context context) {
        if (!readFlag)
            return null;
        readFlag = false;

        File cacheFile = FileUtils.getDisCacheDir(context, "NewsBean" + this.value);
        List<NewsBean> list = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(cacheFile));
            list = (ArrayList<NewsBean>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            Log.e(TAG, "出错了");
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "执行了");
        return list;
    }

    /**
     * 只有第一次才会将数据缓存入文件
     */
    private void saveToFile(Context context, List<NewsBean> list, String fileName) {
        if (!writeFlag)
            return;
        writeFlag = false;

//        File cacheFile = FileUtils.getDisCacheDir(context, "NewsBean" + this.value);
        File cacheFile = FileUtils.getDisCacheDir(context, fileName);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
            oos.writeObject(list);
            writeFlag = false;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e(TAG, "save执行了");
    }


    //-------------------------interface------------------------//
    public interface GetNewsStatus<T> {
        void onSuccess(T newsList);

        void onFailed(Exception e);

        void onFailed();
    }

}
