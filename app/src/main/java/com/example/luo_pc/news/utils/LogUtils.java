package com.example.luo_pc.news.utils;

import android.util.Log;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/7.
 */
public class LogUtils {
    private static boolean debug = true;

    public static void setDebug(boolean d) {
        debug = d;
    }

    private static void e(String TAG, String msg) {
        Log.e(TAG, msg);
    }

    private static void v(String TAG, String msg) {
        Log.v(TAG, msg);
    }

    private static void w(String TAG, String msg) {
        Log.w(TAG, msg);
    }

    private static void d(String TAG, String msg) {
        Log.d(TAG, msg);
    }

    private static void i(String TAG, String msg) {
        Log.i(TAG, msg);
    }

}
