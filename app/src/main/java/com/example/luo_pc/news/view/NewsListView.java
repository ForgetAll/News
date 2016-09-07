package com.example.luo_pc.news.view;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/7.
 */
public interface NewsListView<T> {
    /**
     * 处理数据
     */
    void dealWithNews(T newsList);

    /**
     * 获取数据失败
     */
    void onFaile(Exception e);
    /**
     * 从文件读取数据失败
     */
    void onFaile();
}
