package com.example.luo_pc.news.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.adapter.ImageListAdapter;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.presenter.ImageListPresenter;
import com.example.luo_pc.news.view.ImageListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/10.
 * desc:看图的Fragment
 */
public class ImageListFragment extends Fragment implements ImageListView<List<ImageBean>> {
    private static final String TAG = "ImageListFragment";
    private RecyclerView rcImageList;
    private Context context;
    private ImageListAdapter imageListAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ImageListPresenter imageListPresenter;
    private int pageIndex = 0;
    private List<ImageBean> imageList = null;

    //---------------------------------------system callback-------------------------------//

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        imageListPresenter = new ImageListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, null);
        rcImageList = (RecyclerView) view.findViewById(R.id.rv_image);
        imageListAdapter = new ImageListAdapter(context);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rcImageList.setLayoutManager(staggeredGridLayoutManager);
        rcImageList.setAdapter(imageListAdapter);
        rcImageList.addOnScrollListener(scrollListener);
        imageListPresenter.getImageList(pageIndex, context);
        rcImageList.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    //------------------------------------implements method----------------------------------//

    @Override
    public void dealWith(List<ImageBean> imageList) {
        if (this.imageList == null)
            this.imageList = new ArrayList<ImageBean>();

        if (imageList.get(imageList.size() - 1).getUrl().equals("asdf")) {
            imageList.remove(imageList.size() - 1);
            this.imageList.addAll(imageList);
            imageListAdapter.setData(this.imageList);
            pageIndex++;
            Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imageList.get(imageList.size() - 1).getUrl().equals("asdf")) {
            this.imageList.addAll(imageList);
            imageListAdapter.setData(this.imageList);
            pageIndex++;
            return;
        }

        this.imageList.addAll(imageList);
        imageListAdapter.setData(this.imageList);
        pageIndex++;
    }

    @Override
    public void dealWithNull() {

    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        private int[] a = new int[2];

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            staggeredGridLayoutManager.findLastVisibleItemPositions(a);
            staggeredGridLayoutManager.invalidateSpanAssignments();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //SCROLL_STATE_IDLE
            //The RecyclerView is not currently scrolling.
            if (imageListAdapter.getisShow() && newState == RecyclerView.SCROLL_STATE_IDLE &&
                    a[1] + 1 == imageListAdapter.getItemCount()) {
                //加载更多
                pageIndex++;
                imageListPresenter.getImageList(pageIndex, context);
            }
        }
    };
}
