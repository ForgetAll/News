package com.example.luo_pc.news.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luo-pc on 2016/6/13.
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    /**
     建Province表sql语句
     */
//    public static final String CREATE_PROVINCE = "create table Province(_id integer primary key autoincrement,province_name varchar(10),province_code varchar(15))";
    /**
     * 建City表sql语句
     */
//    public static final String CREATE_CITY = "create table City(_id integer primary key autoincrement,city_name varchar(10),city_code text,province_id varchar(15))";

    /**
     * 建County表sql语句
     */
//    public static final String CREATE_COUNTY = "create table County(_id integer primary key autoincrement,county_name varchar(10),county_code text,city_id varchar(15)),weathercode varchar(20))";

    public MyDatabaseOpenHelper(Context context){
        super(context,"info.db",null,1);
    }

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 我的数据库文件使用另外一个程序写好的，通过用sax解析xml文件获取的数据库，
     * 在这个程序里，已经解析好的数据库在raw包下，在需要用到这个数据库时会通过
     * io将文件写到默认的数据库文件下，之后会通过这个类来获取这个数据库。
     * 如果你对解析xml文件有兴趣的话，我也将这个xml文件放在了raw下，你可以
     * 尝试自己解析。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
