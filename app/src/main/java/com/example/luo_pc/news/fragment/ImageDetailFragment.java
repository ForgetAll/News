package com.example.luo_pc.news.fragment;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.luo_pc.news.R;

import java.net.URL;

/**
 * Created by luo-pc on 2016/7/8.
 */
public class ImageDetailFragment extends DialogFragment implements View.OnLongClickListener {

    private ImageView iv_image_detail;
    private String imageUrl;
    private String desc;
    private ProgressBar pb_image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_image_detail, null);
        iv_image_detail = (ImageView) view.findViewById(R.id.iv_image_detail);
        pb_image = (ProgressBar) view.findViewById(R.id.pb_image);

        iv_image_detail.setOnLongClickListener(this);

        Glide.with(getContext()).load(imageUrl)
                .error(R.drawable.ic_image_loadfail).crossFade().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                iv_image_detail.setImageDrawable(resource);
                pb_image.setVisibility(View.GONE);
            }
        });

//        iv_image_detail.setOnClickListener();
//        setStyle(STYLE_NORMAL, R.style.CustomDialog);

        return view;
    }

    public void setData(String imageUrl, String desc) {
        this.imageUrl = imageUrl;
        this.desc = desc;
    }

    @Override
    public boolean onLongClick(View v) {
        AlertDialog.Builder alter = new AlertDialog.Builder(getActivity());
        alter.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveImageToSysAlbum();

            }
        });

        alter.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alter.setMessage("是否保存图片").create().show();

        return true;
    }

    /**
     * 保存图片
     */
    private void SaveImageToSysAlbum() {
        GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) iv_image_detail.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null) {
            try {
                ContentResolver cr = getActivity().getContentResolver();
                String path = MediaStore.Images.Media.insertImage(cr, bitmap, String.valueOf(System.currentTimeMillis()), desc);
                final String absolutePath = getFilePathByContentResolver(getActivity(), Uri.parse(path));

                if (path != null) {
                    Toast.makeText(getActivity(), "已保存到" + absolutePath, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + absolutePath));
                    //发送广播更新系统相册
                    getActivity().sendBroadcast(intent);
                } else {
                    Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                }
                Log.i("hello", path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 保存图片
     */
    private void SaveImageToSysAlbum(String fileName, String path) {
        GlideBitmapDrawable bitmapDrawable = (GlideBitmapDrawable) iv_image_detail.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null) {
            try {
                final String absolutePath = getFilePathByContentResolver(getActivity(), Uri.parse(path));
                if (path != null) {
                    Toast.makeText(getActivity(), "已保存到" + absolutePath, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(absolutePath));
                    getActivity().sendBroadcast(intent);
                } else {
                    Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                }
                Log.i("hello", path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取绝对路径
     */
    private String getFilePathByContentResolver(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (cursor == null) {
            throw new IllegalArgumentException("Query on " + uri + "returns null result.");
        }
        try {
            if ((cursor.getCount() != 1) || !cursor.moveToFirst()) {

            } else {
                filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
        } finally {
            cursor.close();
        }
        return filePath;
    }


}
