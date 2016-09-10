package com.example.luo_pc.news.model;

import android.content.Context;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/6.
 *
 * desc:此接口用来获取新闻数据集合
 */
public interface NewsListModel<T> {
    void getNews(int pageIndex, String type, NewsListModelImpl.GetNewsStatus<T> getNewsStatus, Context context);
}
