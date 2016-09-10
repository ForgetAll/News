package com.example.luo_pc.news.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luo_pc.news.R;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/10.
 */
public class ImageListFragment extends Fragment {

    private RecyclerView rcImageList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, null);
        rcImageList = (RecyclerView) view.findViewById(R.id.rv_image);

        return view;
    }

}
