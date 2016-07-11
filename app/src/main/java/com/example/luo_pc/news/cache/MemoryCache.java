package com.example.luo_pc.news.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/7/7.
 */
public class MemoryCache {
    private final String TAG = "BitmapMemoryCache";
    private static MemoryCache memoryCache;
    private final LruCache<String, ArrayList> lruCache;

//        {
//            @Override
//            protected int sizeOf(String key, ArrayList list) {
//                return value.getRowBytes() * value.getHeight() / 1024;
//            }
//        };

    private MemoryCache(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int catchSize = maxMemory / 8;
        lruCache = new LruCache<String, ArrayList>(catchSize);
    }

    public synchronized static MemoryCache getInstance(){
        if(memoryCache == null){
            memoryCache = new MemoryCache();
        }
        return memoryCache;
    }

    public void addArrayListToMemory(String key, ArrayList list) {
        if (getArrayListFromMemory(key) == null) {
            lruCache.put(key, list);
        }
    }

    public ArrayList getArrayListFromMemory(String key) {
        return lruCache.get(key);
    }
}



