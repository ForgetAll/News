package com.example.luo_pc.news.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/8/29.
 *
 * 实现懒加载的Fragment
 */
public abstract class BaseLazyFragment extends Fragment {

    protected View mRootView;
    protected Context mContext;
    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;

    //--------------------system method callback------------------------//

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        initPrepare();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState);
        }

        return mRootView;
    }

    //--------------------------------method---------------------------//

    /**
     * 懒加载
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        initData();
        isFirst = false;
    }

    //--------------------------abstract method------------------------//

    /**
     * 在onActivityCreated中调用的方法，可以用来进行初始化操作。
     */
    protected abstract void initPrepare();

    /**
     * fragment被设置为不可见时调用
     */
    protected abstract void onInvisible();

    /**
     * 这里获取数据，刷新界面
     */
    protected abstract void initData();

    /**
     * 初始化布局，请不要把耗时操作放在这个方法里，这个方法用来提供一个
     * 基本的布局而非一个完整的布局，以免ViewPager预加载消耗大量的资源。
     */
    protected abstract View initView(LayoutInflater inflater,
                                     @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState);

    //--------------------------my method------------------------------//

    /**
     * 检查当前网络是否可用
     */
    public boolean isNetworkAvailable() {
        Context context = mContext.getApplicationContext();
        //获取手机所有连接管理对象（包括对wifi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            //获取NetworkInfo对象
            NetworkInfo[] netWorkInfos = connectivityManager.getAllNetworkInfo();
            if (netWorkInfos != null && netWorkInfos.length > 0) {
                for (int i = 0; i < netWorkInfos.length; i++) {
                    //判断当前网络状态是否为连接状态
                    if (netWorkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
