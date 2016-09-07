package com.example.luo_pc.news.model;

import android.content.Context;

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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/6.
 */
public class NewsListModelImpl implements NewsListModel<List<NewsBean>> {
    //why?因为我认为有必要保留此未使用的常量
    @SuppressWarnings("unused")
    private static String TAG = "NewsListModelImpl";

    //why?因为我认为有必要保留此未使用的常量
    @SuppressWarnings("unused")
    private static final int TYPE_CACHE = 1;
    //从网络或者缓存获取数据的标志
    private static final int TYPE_NETWORK = 0;

    private String value;
    private List<NewsBean> newsBeanList;
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
     * <p/>
     * 对于现在我这种rx的用法，恩，太糟了，又出现了迷之缩进
     * 待完善
     */
    private void getData(final int pageIndex, final String type, final GetNewsStatus<List<NewsBean>> getNewsStatus, final Context context) {
        Observable.just(0, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        if (integer == TYPE_NETWORK) {
                            return getUrl(pageIndex, type);
                        }
                        return "cache";
                    }
                }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                if (s.equals("cache")) {
                    if (readFlag) {
                        List<NewsBean> newsListFromFile = getNewsListFromFile(context);
                        if (newsListFromFile == null)
                            getNewsStatus.onFailed();
                        getNewsStatus.onSuccess(newsListFromFile);
                        readFlag = false;
                    } else {
                        getNewsStatus.onFailed();
                    }
                } else {
                    String url = getUrl(pageIndex, type);
                    OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            getNewsStatus.onFailed(e);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            newsBeanList = JsonUtils.readJsonNewsBean(response, value);
                            getNewsStatus.onSuccess(newsBeanList);
                            if (writeFlag) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveToFile(context, newsBeanList);
                                    }
                                }).start();
                            }
                            onCompleted();
                        }
                    });
                }
            }
        });
    }

    /**
     * 从文件中读取数据，此方法不处理线程问题
     * 消除这里的警告因为如果list是个空值，我会处理
     */
    @SuppressWarnings("unchecked")
    private List<NewsBean> getNewsListFromFile(Context context) {
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
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 缓存
     */
    private void saveToFile(Context context, List<NewsBean> list) {
        File cacheFile = FileUtils.getDisCacheDir(context, "NewsBean" + this.value);
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
    }


    //-------------------------interface------------------------//
    public interface GetNewsStatus<T> {
        void onSuccess(T newsList);

        void onFailed(Exception e);

        void onFailed();
    }

}
