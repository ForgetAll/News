package com.example.luo_pc.news.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.adapter.ImageListAdapter;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.cache.MemoryCache;
import com.example.luo_pc.news.utils.FileUtils;
import com.example.luo_pc.news.utils.HttpUtils;
import com.example.luo_pc.news.utils.JsonUtils;
import com.example.luo_pc.news.utils.Urls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by luo-pc on 2016/7/6.
 */
public class ImageFragment extends Fragment implements ImageListAdapter.OnItemClickListener {
    private static final String TAG = "ImageFragment";

    private RecyclerView rv_image;
    //页数
    int pageIndex = 1;
    //第一次打开缓存
    int count = 0;

    private ArrayList<ImageBean> imageList;
    private ImageListAdapter imageListAdapter;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                String name = info.getTypeName();
                if (name.equals("WIFI")) {
                    //初始的时候页数为1
                    new DownloadTask().execute(Urls.IMAGE_URL + pageIndex);
                } else {
                    //没有wifi时提示
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示");
                    builder.setMessage("当前网络需要耗费您的流量加载图片，是否看图？");
                    builder.setPositiveButton("加载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //初始的时候页数为1
                            new DownloadTask().execute(Urls.IMAGE_URL + pageIndex);
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fl_content, new ParentFragment()).commit();
                            FragmentTransaction transition = getActivity().getSupportFragmentManager().beginTransaction();
                            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("newsListFragment");
                            if (fragment != null) {
                                transition.show(fragment);
                            } else {
                                ParentFragment newsListFragment = new ParentFragment();
                                transition.add(R.id.fl_content, newsListFragment, "newsListFragment");
                            }

                            Fragment weatherFragment = getActivity().getSupportFragmentManager().findFragmentByTag("weatherFragment");
                            Fragment imageFragment = getActivity().getSupportFragmentManager().findFragmentByTag("imageFragment");
                            Fragment meFragment = getActivity().getSupportFragmentManager().findFragmentByTag("meFragment");
//                            Fragment settingFragment = getActivity().getSupportFragmentManager().findFragmentByTag("weatherFragment");

                            if (weatherFragment != null) {
                                transition.hide(weatherFragment);
                            }
                            if (imageFragment != null) {
                                transition.hide(imageFragment);
                            }
                            if (meFragment != null) {
                                transition.hide(meFragment);
                            }
                            transition.commit();
                        }
                    });

                    builder.show();
                }
            } else {
                Toast.makeText(getActivity(), "当前无网络！", Toast.LENGTH_SHORT).show();
            }


        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, null);
        rv_image = (RecyclerView) view.findViewById(R.id.rv_image);

        initData();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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

        ObjectInputStream ois = null;
        if (getActivity() != null) {
            File imageCache = FileUtils.getDisCacheDir(getActivity(), "ImageBean");
            try {
                ois = new ObjectInputStream(new FileInputStream(imageCache));
                imageList = (ArrayList<ImageBean>) ois.readObject();
                imageListAdapter.setData(imageList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        new DownloadTask().execute(Urls.IMAGE_URL + pageIndex);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        ImageDetailFragment imageDetailFragment = new ImageDetailFragment();
        imageDetailFragment.setData(imageList.get(position).getUrl(), imageList.get(position).getDesc());
        imageDetailFragment.show(getFragmentManager(), "ImageDetailFragment");

    }


    private void initData() {
        imageListAdapter = new ImageListAdapter(getContext());
    }


    /**
     * 加载更多
     */
    class UpdateTask extends AsyncTask<String, Integer, ArrayList<ImageBean>> {

        private ArrayList<ImageBean> updateNewsList;

        @Override
        protected ArrayList<ImageBean> doInBackground(final String... params) {
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
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
                }
            } else {
//                imageListAdapter.setisShow(false);
                imageListAdapter.setData(ImageFragment.this.imageList);
                imageListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 加载
     */
    class DownloadTask extends AsyncTask<String, Integer, ArrayList<ImageBean>> {

        private ObjectOutputStream oos;

        @Override
        protected ArrayList<ImageBean> doInBackground(final String... params) {
            try {
                String imageUrl = params[0];
                HttpUtils.getJsonString(imageUrl, new HttpUtils.HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        if (JsonUtils.readJsonImageBean(response) != null) {
                            imageList = JsonUtils.readJsonImageBean(response);
//                            memoryCache.addArrayListToMemory("imageList", imageList);

                            if (count == 0) {
                                //序列化imageList
                                if (getActivity() != null) {
                                    File imageCache = FileUtils.getDisCacheDir(getActivity(), "ImageBean");
                                    try {
                                        oos = new ObjectOutputStream(new FileOutputStream(imageCache));
                                        oos.writeObject(imageList);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (oos != null) {
                                            try {
                                                oos.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                count++;
                            }
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
                if (getContext() != null) {
                    Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            } else {
                ImageFragment.this.imageList = imageBeen;
                imageListAdapter.setData(imageBeen);
                imageListAdapter.setisShow(true);
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