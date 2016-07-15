package com.example.luo_pc.news.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.luo_pc.news.R;
import com.example.luo_pc.news.adapter.LocationAdapter;
import com.example.luo_pc.news.application.ManageApplication;
import com.example.luo_pc.news.bean.County;
import com.example.luo_pc.news.cache.DiskLruCache;
import com.example.luo_pc.news.dao.DBUtils;
import com.example.luo_pc.news.dao.MyDatabaseOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/6/23.
 */
public class CountyManage extends AppCompatActivity {
    private final String TAG = "CountyManage";

    private ListView lv_county;
    private DBUtils dbUtils;
    private ArrayList<County> counties;
    private SQLiteDatabase db;
//    private DiskLruCache diskLruCache = null;
    private DiskLruCache.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_list);
        setTitle(getIntent().getStringExtra("city name"));
        ((ManageApplication) getApplication()).addToList(CountyManage.this);
        LocationAdapter countyAdapter = new LocationAdapter(this);
        initView();
        initData();

//        try {
//            File cacheDir = getDiskCacheDir(getApplicationContext(), "weather");
//            if (!cacheDir.exists()) {
//                cacheDir.mkdirs();
//            }
//            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(getApplicationContext()), 1, 1024 * 1024 * 4);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            editor = diskLruCache.edit("weathercode");
//            if (editor != null) {
//                OutputStream outputStream = editor.newOutputStream(1024 * 1024 * 4);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        countyAdapter.setCounties(counties);
        lv_county.setAdapter(countyAdapter);

        lv_county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CountyManage.this, MainActivity.class);
                intent.putExtra("weather code", counties.get(position).getWeathercode());
                intent.putExtra("jump to weather", true);
                SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("weather code",counties.get(position).getWeathercode()).commit();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        lv_county = (ListView) findViewById(R.id.lv_location);
    }

    private void initData() {
        dbUtils = DBUtils.getInstance(getDatabase());
        counties = dbUtils.loadCounty(getIntent().getStringExtra("city code"));
        Log.i(TAG, getIntent().getStringExtra("city code"));
    }

    private SQLiteDatabase getDatabase() {
        MyDatabaseOpenHelper mdb = new MyDatabaseOpenHelper(this);
        db = mdb.getReadableDatabase();
        return db;
    }

    //获取路径
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    //获取版本
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }
}
