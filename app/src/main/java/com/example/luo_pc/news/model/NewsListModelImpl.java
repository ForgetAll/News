package com.example.luo_pc.news.model;

import com.example.luo_pc.news.bean.NewsBean;
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

    private List<NewsBean> newsBeanList;

    //-----------------------interface's method----------------------------//
    @Override
    public List<NewsBean> getNews(int pageIndex, final String type) {
        String url = getUrl(pageIndex, type);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                newsBeanList = JsonUtils.readJsonNewsBean(response, type);
            }
        });

        return newsBeanList;
    }

    //------------------------------my method------------------------------//

    /**
     * 拼接url
     */
    private String getUrl(int pageIndex, String type) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            //头条
            case Urls.TOP_ID:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            //NBA
            case Urls.NBA_ID:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            //汽车
            case Urls.CAR_ID:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            //笑话
            case Urls.JOKE_ID:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }
}
