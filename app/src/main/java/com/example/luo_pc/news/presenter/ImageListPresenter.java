package com.example.luo_pc.news.presenter;

import android.content.Context;

import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.model.ImageListModel;
import com.example.luo_pc.news.model.ImageListModelImpl;
import com.example.luo_pc.news.view.ImageListView;

import java.util.List;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/11.
 * desc:使ImageListFragment和ImageListModel发生交互的
 * 中间件
 */
public class ImageListPresenter implements ImageListModelImpl.DispatchImage<List<ImageBean>> {
    ImageListModel imageListModel;
    ImageListView<List<ImageBean>> imageListView;

    public ImageListPresenter(ImageListView<List<ImageBean>> imageListView) {
        this.imageListView = imageListView;
        imageListModel = new ImageListModelImpl();
    }

    //suppress unchecked because I will deal with the null list inside onFailed method
    @SuppressWarnings("unchecked")
    public void getImageList(int pageIndex, Context context) {
        imageListModel.getImageList(pageIndex, context, this);
    }

    //-----------------------------implements method-----------------------------//
    @Override
    public void onSuccess(List<ImageBean> newsList) {
        imageListView.dealWith(newsList);
    }

    @Override
    public void onFailed() {
        imageListView.dealWithNull();
    }
}
