package com.example.luo_pc.news.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.luo_pc.news.R;
import com.example.luo_pc.news.adapter.ImageListAdapter;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.cache.MemoryCache;
import com.example.luo_pc.news.utils.HttpUtils;
import com.example.luo_pc.news.utils.JsonUtils;
import com.example.luo_pc.news.utils.Urls;

import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/7/6.
 */
public class ImageFragment extends Fragment implements ImageListAdapter.OnItemClickListener {
    private static final String TAG = "ImageFragment";

    private RecyclerView rv_image;
    //页数
    int pageIndex = 1;

    private ArrayList<ImageBean> imageList;
    private ImageListAdapter imageListAdapter;

    private MemoryCache memoryCache;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, null);

        rv_image = (RecyclerView) view.findViewById(R.id.rv_image);


        initData();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rv_image.setLayoutManager(staggeredGridLayoutManager);
        rv_image.setAdapter(imageListAdapter);


//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//              @Override
//              public int getSpanSize(int position) {
//                  if(imageListAdapter.getItemViewType(position) == ImageListAdapter.TYPE_FOOTER){
//                      return gridLayoutManager.getSpanCount();
//                  }else{
//                      return 1;
//                  }
//              }
//        });

        //设置间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rv_image.addItemDecoration(decoration);
        rv_image.addOnScrollListener(onScrollListener);

        imageListAdapter.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
        imageDetailFragment.setData(imageList.get(position).getUrl(),imageList.get(position).getDesc());
        imageDetailFragment.show(getFragmentManager(),"ImageDetailFragment");
//        fragment_test ft = new fragment_test();
//        ft.setData(imageList,getActivity(),position);
//        ft.show(getFragmentManager(),"ft");
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), ImageDialogActivity.class);
//        ImageDialogActivity.imageList = imageList;
//        ImageDialogActivity.position = position;
//        startActivity(intent);

    }


    private void initData() {
        imageListAdapter = new ImageListAdapter(getContext());
        //初始的时候页数为1
        new DownloadTask().execute(Urls.IMAGE_URL + pageIndex);

    }

    private void initCache() {
        memoryCache = MemoryCache.getInstance();
    }


    class UpdateTask extends AsyncTask<String, Integer, ArrayList<ImageBean>> {

        private ArrayList<ImageBean> updateNewsList;

        @Override
        protected ArrayList<ImageBean> doInBackground(String... params) {
            try {
                String infoUrl = params[0];
                HttpUtils.getJsonString(infoUrl, new HttpUtils.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        updateNewsList = JsonUtils.readJsonImageBean(response);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });

                if (updateNewsList == null) {
                    imageListAdapter.setisShow(false);
                    return null;
                }

                for (ImageBean i : updateNewsList) {
                    imageList.add(i);
                }

                return updateNewsList;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageBean> updateNewsList) {
            if (updateNewsList == null) {
                Toast.makeText(getContext(), "请求数据失败", Toast.LENGTH_SHORT);
                return;
            } else {
//                imageListAdapter.setisShow(false);
                imageListAdapter.setData(ImageFragment.this.imageList);
                imageListAdapter.notifyDataSetChanged();
            }
        }
    }

    class DownloadTask extends AsyncTask<String, Integer, ArrayList<ImageBean>> {
        @Override
        protected ArrayList<ImageBean> doInBackground(String... params) {
            try {
                String imageUrl = params[0];
                HttpUtils.getJsonString(imageUrl, new HttpUtils.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        if (JsonUtils.readJsonImageBean(response) != null) {
                            imageList = JsonUtils.readJsonImageBean(response);
//                            memoryCache.addArrayListToMemory("imageList", imageList);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
                return imageList;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ImageBean> imageBeen) {
            if (imageBeen == null) {
                Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT);
            } else {
                ImageFragment.this.imageList = imageBeen;
                imageListAdapter.setData(imageBeen);
                imageListAdapter.setisShow(true);
                imageListAdapter.notifyDataSetChanged();
            }
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //SCROLL_STATE_IDLE
            //The RecyclerView is not currently scrolling.
            if (imageListAdapter.getisShow() && newState == RecyclerView.SCROLL_STATE_IDLE) {
                //加载更多

                pageIndex++;
                new UpdateTask().execute(Urls.IMAGE_URL + pageIndex);
            }
            staggeredGridLayoutManager.invalidateSpanAssignments();

        }
    };

}



