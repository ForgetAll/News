package com.example.luo_pc.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;


import com.example.luo_pc.news.R;
import com.example.luo_pc.news.application.ManageApplication;
import com.example.luo_pc.news.fragment.ImageFragment;
import com.example.luo_pc.news.fragment.MeFragment;
import com.example.luo_pc.news.fragment.ParentFragment;
import com.example.luo_pc.news.fragment.SettingFragment;
import com.example.luo_pc.news.fragment.WeatherFragment;
import com.example.luo_pc.news.service.CheckForNetWork;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView nav_layout;
    private final String TAG = "MainActivity";
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    //判断是否跳转到天气
    private boolean jump;
    //包含数据的intent
    private Intent myIntent;

    //Fragment
    private ParentFragment newsListFragment = null;
    private WeatherFragment weatherFragment = null;
    private ImageFragment imageFragment = null;
    private MeFragment meFragment = null;
    private SettingFragment settingFragment = null;

    private FragmentTransaction transaction;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.fragment_main);

        initData();
        transaction = getSupportFragmentManager().beginTransaction();

        jumpToWeather();

        sp = getSharedPreferences("config", MODE_PRIVATE);
        edit = sp.edit();


        //这里的逻辑很容易搞混，写过一次数据库就不用再写了
        if (!sp.getBoolean("write once", false)) {
            edit.putBoolean("firstOpen", true).apply();
        } else {
            edit.putBoolean("firstOpen", false).apply();
        }

        initView();

        nav_layout.setNavigationItemSelectedListener(this);
    }

    /**
     * MainActivity被销毁时跳转到天气的逻辑
     */
    public void jumpToWeather(){
        if (getIntent().getBooleanExtra("jump to weather", false)) {
//            ArrayList<Activity> list = ((ManageApplication) getApplication()).getList();
//            /*
//            *  其实采用singletask启动的activity复用的时候会将他上面的activity出栈，但我这里
//            *  想练习一下application级别变量的使用
//            */
//            for (Activity i : list) {
//                i.finish();
//                list.remove(i);
//            }
            weatherFragment = new WeatherFragment();
            transaction.add(R.id.fl_content, weatherFragment);

            if (meFragment != null) {
                transaction.hide(meFragment);
            }
            if (newsListFragment != null) {
                transaction.hide(newsListFragment);
            }
            if (imageFragment != null) {
                transaction.hide(imageFragment);
            }
            if(settingFragment != null){
                transaction.hide(settingFragment);
            }

//            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment()).commit();
        } else {
            //初始是新闻页面
            newsListFragment = new ParentFragment();
            transaction.add(R.id.fl_content, newsListFragment, "newsListFragment").commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        jumpToWeather(jump);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void handleIntent(Intent intent) {
        jump = intent.getBooleanExtra("jump to weather", false);
        myIntent = intent;
    }


    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_layout = (NavigationView) findViewById(R.id.nav_layout);

    }

    private void initData() {
        //开启服务监听网络状态
        Intent startIntent = new Intent();
        startIntent.setClass(this, CheckForNetWork.class);
        startService(startIntent);

//        fragmentList.add(settingFragment);

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_news:
                item.setChecked(true);
                transaction = getSupportFragmentManager().beginTransaction();
                fragment = getSupportFragmentManager().findFragmentByTag("newsListFragment");
                if (fragment != null) {
                    transaction.show(newsListFragment);
                } else {
                    transaction.add(R.id.fl_content, newsListFragment, "newsListFragment");
                }

                if (weatherFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("weatherFragment"));
                }
                if (imageFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("imageFragment"));
                }
                if (meFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("meFragment"));
                }
                if(settingFragment != null){
                    transaction.hide(settingFragment);
                }

                transaction.commit();

//                transaction.add(newsListFragment,"newsListFragment").commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ParentFragment)).commit();
                drawer.closeDrawers();
                break;

            case R.id.nav_weather:
                transaction = getSupportFragmentManager().beginTransaction();
                fragment = getSupportFragmentManager().findFragmentByTag("weatherFragment");
                boolean firstOpen = sp.getBoolean("firstOpen", false);

                if (firstOpen) {//如果是第一次打开，则将raw下的数据库写到文件里
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            writeDatabase();
                        }
                    }).start();
                    edit.putBoolean("firstOpen", false).commit();
                    edit.putBoolean("write once", true).commit();
                }

                if (fragment != null) {
                    transaction.show(weatherFragment);
                } else {
                    weatherFragment = new WeatherFragment();
                    transaction.add(R.id.fl_content, weatherFragment, "weatherFragment");
                }

                if (newsListFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("newsListFragment"));
                }
                if (imageFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("imageFragment"));
                }
                if (meFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("meFragment"));
                }
                if(settingFragment != null){
                    transaction.hide(settingFragment);
                }

                transaction.commit();

//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment()).commit();
                drawer.closeDrawers();
                break;

            case R.id.nav_picture:
                transaction = getSupportFragmentManager().beginTransaction();
                fragment = getSupportFragmentManager().findFragmentByTag("imageFragment");
                if (fragment != null) {
                    transaction.show(imageFragment);
                } else {
                    imageFragment = new ImageFragment();
                    transaction.add(R.id.fl_content, imageFragment, "imageFragment");
                }


                if (weatherFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("weatherFragment"));
                }
                if (newsListFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("newsListFragment"));
                }
                if (meFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("meFragment"));
                }
                if(settingFragment != null){
                    transaction.hide(settingFragment);
                }

                transaction.commit();

//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ImageFragment()).commit();
                drawer.closeDrawers();
//                Log.i(TAG,"caonima");
                break;

            case R.id.nav_me:
                transaction = getSupportFragmentManager().beginTransaction();

                if (meFragment != null) {
                    transaction.show(meFragment);
                } else {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_content, meFragment, "meFragment");
                }

                if (weatherFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("weatherFragment"));
                }
                if (newsListFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("newsListFragment"));
                }
                if (imageFragment != null) {
                    transaction.hide(getSupportFragmentManager().findFragmentByTag("imageFragment"));
                }
                if(settingFragment != null){
                    transaction.hide(settingFragment);
                }

                transaction.commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new MeFragment()).commit();
                drawer.closeDrawers();
                break;

            case R.id.nav_setting:
                transaction = getSupportFragmentManager().beginTransaction();
                if(settingFragment != null){
                    transaction.show(settingFragment);
                }else{
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.fl_content,settingFragment,"settingFragment");
                }

                if (weatherFragment != null) {
                    transaction.hide(weatherFragment);
                }
                if (newsListFragment != null) {
                    transaction.hide(newsListFragment);
                }
                if (imageFragment != null) {
                    transaction.hide(imageFragment);
                }
                if(meFragment != null){
                    transaction.hide(meFragment);
                }

                transaction.commit();

                break;
        }
        return true;
    }



    /**
     * 将raw目录下的info.db写到databases文件下
     */
    private void writeDatabase() {
//        final String DATABASE_PATH = getFilesDir().getAbsolutePath();
        final String DATABASE_PATH = "/data/data/com.example.luo_pc.news/databases";
        String DATABASE_FILENAME = "info.db";
        String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
        try {
            File dir = new File(DATABASE_PATH);
            if (!dir.exists()) {//如果文件夹不存在创建文件夹
                dir.mkdirs();
            }
            File file = new File(databaseFilename);
            if (!file.exists()) {//如果文件不存在创建文件
                file.createNewFile();
                InputStream is = MainActivity.this.getResources().openRawResource(R.raw.info);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 在County跳转到MainActivity之后展示weatherFragment
     * @param jump 是否跳转
     */
    public void jumpToWeather(boolean jump) {
        if (jump) {
            ArrayList<Activity> list = ((ManageApplication) getApplication()).getList();
            for (Activity i : list) {
                i.finish();
            }

            transaction = getSupportFragmentManager().beginTransaction();
            weatherFragment.onRefresh(myIntent.getStringExtra("weather code"));
            transaction.show(weatherFragment);

            if (meFragment != null) {
                transaction.hide(meFragment);
                Log.i(TAG, "hide me");
            }
            if (newsListFragment != null) {
                transaction.hide(newsListFragment);
                Log.i(TAG, "hide news");
            }
            if (imageFragment != null) {
                transaction.hide(imageFragment);
                Log.i(TAG, "hide image");
            }

            transaction.commit();

//            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment(myIntent)).commit();
        }
    }


}