package com.example.luo_pc.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.activity.NewsActivity;
import com.example.luo_pc.news.adapter.NewsListAdapter;
import com.example.luo_pc.news.bean.NewsBean;
import com.example.luo_pc.news.presenter.NewsPresenter;
import com.example.luo_pc.news.view.NewsListView;

import java.util.List;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/6.
 */
public class NewsListFragment2 extends BaseLazyFragment implements NewsListView<List<NewsBean>>, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NewsListFragment2";

    private List<NewsBean> newsList;
    private NewsPresenter newsPresenter;
    private SwipeRefreshLayout refresh;
    private RecyclerView rcNewsList;
    private int pageIndex = 0;
    private String mType;

    //type
    public static final String TOP_FRAGMENT = "TopFragment";
    public static final String CAR_FRAGMENT = "CarFragment";
    public static final String NBA_FRAGMENT = "NBAFragment";
    public static final String JOKE_FRAGMENT = "JokeFragment";
    private NewsListAdapter adapter;
    private LinearLayoutManager manager;

    //----------------------------implements abstract method--------------------------------//

    @Override
    protected void initPrepare() {
        newsPresenter = new NewsPresenter(this);
        mType = getArguments().getString("type");
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void initData() {
//        newsPresenter.getNews(pageIndex, mType);
        onRefresh();
    }

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, null);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.sr_refresh);
        rcNewsList = (RecyclerView) view.findViewById(R.id.recycle_news);
        refresh.setOnRefreshListener(this);

        manager = new LinearLayoutManager(mContext);
        adapter = new NewsListAdapter(mContext);
        rcNewsList.setLayoutManager(manager);
        rcNewsList.setAdapter(adapter);
        rcNewsList.addOnScrollListener(scrollListener);
        adapter.setOnItemClickListener(itemClickListener);
        return view;
    }

    //--------------------------------interface's method----------------------------//

    @Override
    public void dealWithNews(List<NewsBean> newsList) {
        adapter.isShowFooter(true);
        if (this.newsList == null) {
            this.newsList = newsList;
        } else {
            //都写一样的名字……差点被自己坑了
            if (newsList == null || newsList.size() == 0) {
                adapter.isShowFooter(false);
            } else
                this.newsList.addAll(newsList);
        }
        adapter.setData(this.newsList);
        pageIndex += 20;
    }

    @Override
    public void onFaile(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onFaile() {
        //暂时没想好做什么处理
    }

    //--------------------------------system and constructor-------------------------------//

    public static NewsListFragment2 newInstance(String type) {
        Bundle args = new Bundle();
        NewsListFragment2 fragment2 = new NewsListFragment2();
        args.putString("type", type);
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onDestroy() {
        newsPresenter = null;
        super.onDestroy();
    }

    //--------------------------------my  method------------------------------------//

    @Override
    public void onRefresh() {
        pageIndex = 0;
        if (newsList != null) {
            newsList.clear();
        }
        newsPresenter.getNews(pageIndex, mType, mContext);
        refresh.setRefreshing(false);
    }

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = manager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isShowFooter()) {
                //加载更多
                newsPresenter.getNews(pageIndex + 20, mType, mContext);
            }
        }
    };

    NewsListAdapter.OnItemClickListener itemClickListener = new NewsListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent();
            intent.setClass(mContext, NewsActivity.class);
            intent.putExtra("url", newsList.get(position).getUrl_3w());
            startActivity(intent);
        }
    };


}
