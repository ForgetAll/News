package com.example.luo_pc.news.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.luo_pc.news.bean.City;
import com.example.luo_pc.news.bean.County;
import com.example.luo_pc.news.bean.Province;

import java.util.ArrayList;
import java.util.List;


/**
 * 暂时有问题，不用了，数据库直接获取，不通过dbhelper来弄了
 * Created by luo-pc on 2016/6/13.
 */
public class DBUtils {
    private static final String TAG = "DBUtils";
    private static DBUtils dbUtils;

    private SQLiteDatabase db;

    /**
     * 采用单例模式
     */
    private DBUtils(SQLiteDatabase db) {
//        MyDatabaseOpenHelper dbHelper = new MyDatabaseOpenHelper(context);//在MyDatabaseOpenHelper的这个构造方法中都已经写死了
        this.db = db;
    }

    /**
     * 获取实例
     */
    public synchronized static DBUtils getInstance(SQLiteDatabase db) {
        if (dbUtils == null) {
            dbUtils = new DBUtils(db);
        }
        return dbUtils;
    }

    /**
     * 将Province实例存储到数据库
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("province_code", province.getProvinceCode());
            db.insert("Province", null, values);
        }
    }

    /**
     * 将City实例存储到数据库
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    /**
     * 将County实例存储到数据库
     * _id integer primary key autoincrement,county_name text,county_code text,city_id integer
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            values.put("weathercode",county.getWeathercode());
            db.insert("County", null, values);
        }
    }

    /**
     * 读取Province表信息
     */
    public ArrayList<Province> loadProvinces() {
        ArrayList<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 读取City表信息
     */
    public ArrayList<City> loadCity(String provinceId) {
        ArrayList<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 读取County表信息
     */
    public ArrayList<County> loadCounty(String cityId) {
        ArrayList<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setWeathercode(cursor.getString(cursor.getColumnIndex("weathercode")));
                list.add(county);
            }
            cursor.close();
        }
        return list;
    }
}
