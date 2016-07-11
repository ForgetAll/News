package com.example.luo_pc.news.application;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/6/23.
 */
public class ManageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        list = new ArrayList<>();
    }

    //用来管理选择城市activity销毁
    private ArrayList<Activity> list;

    public void addToList(Activity activity){
        list.add(activity);
    }

    public ArrayList<Activity> getList() {
        return list;
    }



}
