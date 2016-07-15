package com.example.luo_pc.news.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by luo-pc on 2016/7/15.
 */
public class FileUtils {
    public static File getDisCacheDir(Context context, String data) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + data);
    }


}
