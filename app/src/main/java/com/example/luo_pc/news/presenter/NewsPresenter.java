package com.example.luo_pc.news.presenter;

import android.content.Context;
import android.util.Log;

import com.example.luo_pc.news.bean.NewsBean;
import com.example.luo_pc.news.model.NewsListModelImpl;
import com.example.luo_pc.news.view.NewsListView;

import java.util.List;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/7.
 */
public class NewsPresenter implements NewsListModelImpl.GetNewsStatus<List<NewsBean>> {
    private static final String TAG = "NewsPresenter";

    NewsListModelImpl newsListModel;
    NewsListView<List<NewsBean>> newsListView;

    public NewsPresenter(NewsListView<List<NewsBean>> newsListView) {
        this.newsListView = newsListView;
        newsListModel = new NewsListModelImpl();
    }

    public void getNews(int pageIndex, String type, Context context) {
        newsListModel.getNews(pageIndex, type, this, context);
    }

    @Override
    public void onSuccess(List<NewsBean> newsList) {
        Log.e(TAG, "onSuccess");
        newsListView.dealWithNews(newsList);
    }

    @Override
    public void onFailed(Exception e) {
        Log.e(TAG, "onFailed e");
        newsListView.onFaile(e);
    }

    @Override
    public void onFailed() {
        Log.e(TAG, "onFailed");
        newsListView.onFaile();
    }
}
