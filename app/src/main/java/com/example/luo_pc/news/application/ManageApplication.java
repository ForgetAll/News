package com.example.luo_pc.news.application;

import android.app.Activity;
import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by luo-pc on 2016/6/23.
 */
public class ManageApplication extends Application {

    /**
     * 已无必要使用该list
     */
    @Deprecated
    private ArrayList<Activity> list;

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<Activity>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public void addToList(Activity activity) {
        list.add(activity);
    }

    public ArrayList<Activity> getList() {
        return list;
    }


}
