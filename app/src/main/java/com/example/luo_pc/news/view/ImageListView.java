package com.example.luo_pc.news.view;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/10.
 * desc:
 */
public interface ImageListView<T> {
    /**
     * 此处执行处理imageList的逻辑
     */
    void dealWith(T imageList);

    /**
     * 此处执行imageList为空时的逻辑
     */
    void dealWithNull();
}
