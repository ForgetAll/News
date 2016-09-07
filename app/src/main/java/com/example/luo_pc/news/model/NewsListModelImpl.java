package com.example.luo_pc.news.model;

import android.util.Log;

import com.example.luo_pc.news.bean.NewsBean;
import com.example.luo_pc.news.fragment.NewsListFragment2;
import com.example.luo_pc.news.utils.JsonUtils;
import com.example.luo_pc.news.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/6.
 */
public class NewsListModelImpl implements NewsListModel<List<NewsBean>> {
    private static String TAG = "NewsListModelImpl";

    private String value;
    private List<NewsBean> newsBeanList;

    //-----------------------interface's method----------------------------//
    @Override
    public void getNews(int pageIndex, final String type, final GetNewsStatus<List<NewsBean>> getNewsStatus) {
        String url = getUrl(pageIndex, type);
        Log.e(TAG, url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                getNewsStatus.onFailed(e);
            }

            @Override
            public void onResponse(String response, int id) {
                newsBeanList = JsonUtils.readJsonNewsBean(response, value);
                getNewsStatus.onSuccess(newsBeanList);
            }
        });
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
    private void setType(String value){
        this.value = value;
    }

    //-------------------------interface------------------------//
    public interface GetNewsStatus<T> {
        void onSuccess(T newsList);

        void onFailed(Exception e);
    }
}
