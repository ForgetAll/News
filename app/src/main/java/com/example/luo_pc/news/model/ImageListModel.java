package com.example.luo_pc.news.model;

        import android.content.Context;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/10.
 *
 * desc：此接口用来获取图片数据集合
 */
public interface ImageListModel<T> {
    void getImageList(int pageIndex, Context conetxt, ImageListModelImpl.DispatchImage<T> dispatchImage);
}
