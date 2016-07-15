package com.example.luo_pc.news.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by luo-pc on 2016/7/15.
 */
public class DiskLruCacheUtils {
    DiskLruCache diskLruCache = null;

    public DiskLruCache getInstance(Context context) {
        try {
            File cacheDir = getDisCacheDir(context, "data");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 5 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getDisCacheDir(Context context, String data) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + data);
    }

    public int getAppVersion(Context context){
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return 1;
    }



}
