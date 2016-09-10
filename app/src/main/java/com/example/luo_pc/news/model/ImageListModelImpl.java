package com.example.luo_pc.news.model;

import android.content.Context;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.utils.FileUtils;
import com.example.luo_pc.news.utils.JsonUtils;
import com.example.luo_pc.news.utils.Urls;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Luo_xiasuhuei321@163.com on 2016/9/10.
 * desc：此类是ImageListModel的实现类，执行一些具体的代码
 * <p>
 * 约定：从网络获取数据成功，会返回正常的数据集合
 * 从网络获取数据失败，会尝试从缓存读取数据，但是这样
 * 读取成功的集合会在最后一个数据加入一段判定信息。
 */
public class ImageListModelImpl implements ImageListModel<List<ImageBean>> {
    //suppress unused because it will be used sometime you don't know
    @SuppressWarnings("unused")
    private static final String TAG = "ImageListModelImpl";

    private boolean writeFlag = true;
    private boolean readFlag = true;

    //----------------------interface's method----------------------//

    @Override
    public void getImageList(int pageIndex, Context context, DispatchImage<List<ImageBean>> dispatchImage) {
        Observable.just(getUrl(pageIndex)).map(new Func1<String, List<ImageBean>>() {
            private ArrayList<ImageBean> imageList;

            @Override
            public List<ImageBean> call(String s) {
                try {
                    Response response = OkHttpUtils.get().url(getUrl(pageIndex)).build().execute();
                    imageList = JsonUtils.readJsonImageBean(response.body().string());
                    if (imageList != null) {
                        saveToFile(context, imageList);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imageList;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<ImageBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ImageBean> imageBeen) {
                if (imageBeen == null) {
                    List<ImageBean> dataFromFile = getDataFromFile(context);
                    if (dataFromFile == null) {
                        dispatchImage.onFailed();
                        onCompleted();
                    }
                    ImageBean imageBean = new ImageBean();
                    imageBean.setUrl("asdf");
                    dataFromFile.add(imageBean);
                }
                dispatchImage.onSuccess(imageBeen);
                onCompleted();
            }
        });
    }

    //---------------------------my method-------------------------//

    /**
     * 根据pageIndex来获取正确的url
     *
     * @param pageIndex 页
     * @return String
     */
    private String getUrl(int pageIndex) {
        return Urls.IMAGE_URL + pageIndex;
    }

    /**
     * 从文件中读取数据
     *
     * @param context 上下文
     * @return ArrayList<ImageBean>
     */
    private List<ImageBean> getDataFromFile(Context context) {
        if (!readFlag)
            return null;
        File imageCache = FileUtils.getDisCacheDir(context, context.getString(R.string.IMAGE_CACHE));
        List<ImageBean> imageList = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(imageCache));
            imageList = (ArrayList<ImageBean>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        return imageList;
    }

    private void saveToFile(Context context, List<ImageBean> imageList) {
        if (!writeFlag)
            return;
        File imageCache = FileUtils.getDisCacheDir(context, context.getString(R.string.IMAGE_CACHE));
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(imageCache));
            oos.writeObject(imageList);
        } catch (IOException e) {
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


    //---------------------------interface--------------------------//


    public interface DispatchImage<T> {
        void onSuccess(T newsList);

        void onFailed();
    }
}
