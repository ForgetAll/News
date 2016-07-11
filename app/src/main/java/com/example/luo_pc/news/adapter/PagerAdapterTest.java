package com.example.luo_pc.news.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.luo_pc.news.R;
import com.example.luo_pc.news.bean.ImageBean;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/7/9.
 * 测试实现功能用，暂时无用处
 */
public class PagerAdapterTest extends PagerAdapter {
    public static final String TAG = "PagerAdapterTest";

    ArrayList<ImageBean> imageList = null;
    private Context context;
    private ImageView imageView;
    private int position;

    public ImageView getImageView() {
        return imageView;
    }

    public PagerAdapterTest(ArrayList<ImageBean> imageList, Context context, int position) {
        this.imageList = imageList;
        this.context = context;
        this.position = position;
    }

    @Override
    public int getCount() {
        if (imageList == null) {
            return 0;
        } else
            return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.fragment_image_detail, null);
        imageView = (ImageView) view.findViewById(R.id.iv_image_detail);
        Log.i(TAG, "图片的位置" + this.position);
        Log.i(TAG, "当前位置" + position);
        Glide.with(context).load(imageList.get(position).getUrl())
                .error(R.drawable.ic_image_loadfail).crossFade().into(imageView);
        container.addView(view);
        return view;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
