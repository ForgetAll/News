package com.example.luo_pc.news.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.luo_pc.news.R;
import com.example.luo_pc.news.adapter.LocationAdapter;
import com.example.luo_pc.news.application.ManageApplication;
import com.example.luo_pc.news.bean.Province;
import com.example.luo_pc.news.dao.DBUtils;
import com.example.luo_pc.news.dao.MyDatabaseOpenHelper;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/6/23.
 */
public class ProvinceManage extends AppCompatActivity {
    private static final String TAG = "ProvinceMange";
    private ListView lv_province;
    private ArrayList<Province> provinces;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province_list);
        ((ManageApplication) getApplication()).addToList(ProvinceManage.this);
        setTitle("省");
        LocationAdapter provinceAdapter = new LocationAdapter(this);
        initView();
        initData();
        provinceAdapter.setProvinces(provinces);
        lv_province.setAdapter(provinceAdapter);
        lv_province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ProvinceManage.this, CityManage.class);
                intent.putExtra("province code", provinces.get(position).getProvinceCode());
                intent.putExtra("province name", provinces.get(position).getProvinceName());
                Log.i(TAG, provinces.get(position).getProvinceCode());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        lv_province = (ListView) findViewById(R.id.lv_location);
    }

    private void initData() {
        DBUtils dbUtils = DBUtils.getInstance(getDatabase());
        provinces = dbUtils.loadProvinces();
    }

    private SQLiteDatabase getDatabase() {
        MyDatabaseOpenHelper mdb = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = mdb.getReadableDatabase();
        return db;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }
}
