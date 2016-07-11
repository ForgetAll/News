package com.example.luo_pc.news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.luo_pc.news.R;
import com.example.luo_pc.news.adapter.PagerAdapterTest;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.view.WrapViewPager;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/7/9.
 */
public class fragment_test extends DialogFragment implements ViewPager.OnPageChangeListener {
    private static final String TAG = "fragment_test";

    private WrapViewPager vp_test;
    private ArrayList<ImageBean> imageList;
    private Context context;
    private PagerAdapterTest mp;
    private ImageView iv_image_detail;
    private int position;
    int pagePosition = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container);
//        iv_image_detail = (ImageView) getActivity().findViewById(R.id.iv_image_detail);
        vp_test = (WrapViewPager) view.findViewById(R.id.vp_test);
        mp = new PagerAdapterTest(imageList, context, position);

        this.iv_image_detail = mp.getImageView();
        vp_test.setAdapter(mp);

        vp_test.setCurrentItem(this.position);

        vp_test.addOnPageChangeListener(this);
        return view;
    }

    public void setData(ArrayList<ImageBean> imageList, Context context, int position) {
        this.imageList = imageList;
        this.context = context;
        this.position = position;

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
