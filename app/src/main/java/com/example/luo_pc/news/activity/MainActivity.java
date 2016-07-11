package com.example.luo_pc.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;


import com.example.luo_pc.news.R;
import com.example.luo_pc.news.application.ManageApplication;
import com.example.luo_pc.news.fragment.ImageFragment;
import com.example.luo_pc.news.fragment.MeFragment;
import com.example.luo_pc.news.fragment.ParentFragment;
import com.example.luo_pc.news.fragment.WeatherFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView nav_layout;
    private final String TAG = "MainActivity";
    private Context context;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private String weatherCode;
    private boolean jump;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fragment_main);
        context = getApplicationContext();
        Log.i(TAG, "onCreate");

        //如果MainActivity被销毁的话，这段程序用来判断跳转到天气
        if (getIntent().getBooleanExtra("jump to weather", false)) {
            ArrayList<Activity> list = ((ManageApplication) getApplication()).getList();

            /*其实采用singletask启动的activity复用的时候会将他上面的activity出栈，但我这里
             * 想练习一下application级别变量的使用
             */
            for (Activity i : list) {
                i.finish();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment()).commit();
        } else {
            //初始是新闻页面
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ParentFragment()).commit();
        }

        sp = getSharedPreferences("config", MODE_PRIVATE);
        edit = sp.edit();

        /*
         * 这类逻辑很容易搞混，写过一次数据库就不用再写了
         */
        if (!sp.getBoolean("write once", false)) {
            edit.putBoolean("firstOpen", true).commit();
        } else {
            edit.putBoolean("firstOpen", false).commit();
        }


        initView();

        nav_layout.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        jumpToWeather(jump);
    }

    private void handleIntent(Intent intent) {
        weatherCode = intent.getStringExtra("weather code");
        jump = intent.getBooleanExtra("jump to weather", false);
        myIntent = intent;
    }

    public void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_layout = (NavigationView) findViewById(R.id.nav_layout);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ParentFragment()).commit();
                drawer.closeDrawers();
                break;

            case R.id.nav_weather:
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

                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment()).commit();
                drawer.closeDrawers();
                break;

            case R.id.nav_picture:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,new ImageFragment()).commit();
                drawer.closeDrawers();
//                Log.i(TAG,"caonima");
                break;

            case R.id.nav_me:
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new MeFragment()).commit();
                drawer.closeDrawers();
                break;
        }
        return true;
    }


    /*
     * 将raw目录下的info.db写到databases文件下
     */
    private void writeDatabase() {
//        final String DATABASE_PATH = getFilesDir().getAbsolutePath();
        final String DATABASE_PATH = "/data/data/com.example.luo_pc.studyforweb/databases";
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
                int count = 0;
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

//    public void jumpToWeather(Intent intent) {
//        if (intent.getBooleanExtra("jump to weather", false)) {
//            ArrayList<Activity> list = ((ManageApplication) getApplication()).getList();
//            for (Activity i : list) {
//                i.finish();
//            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment(myIntent)).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ParentFragment()).commit();
//        }
//    }

    public void jumpToWeather(boolean jump) {
        if (jump) {
            ArrayList<Activity> list = ((ManageApplication) getApplication()).getList();
            for (Activity i : list) {
                i.finish();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new WeatherFragment(myIntent)).commit();
        }
    }
}