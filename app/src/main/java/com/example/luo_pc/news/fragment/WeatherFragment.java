package com.example.luo_pc.news.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luo_pc.news.R;
import com.example.luo_pc.news.activity.ProvinceManage;
import com.example.luo_pc.news.adapter.WeatherAdapter;
import com.example.luo_pc.news.bean.WeatherBean;
import com.example.luo_pc.news.utils.FileUtils;
import com.example.luo_pc.news.utils.HttpUtils;
import com.example.luo_pc.news.utils.JsonUtils;
import com.example.luo_pc.news.utils.Urls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Created by luo-pc on 2016/6/20.
 */
public class WeatherFragment extends Fragment {
    private final String TAG = "WeatherFragment";

    private TextView tv_city;
    private TextView tv_temp;
    private TextView tv_wd;
    private TextView tv_wp;
    private TextView tv_hum;
    private TextView tv_humdata;
    private TextView tv_air;
    private TextView tv_airlevel;
    private ListView lv_weather;
    private WeatherAdapter weatherAdapter;
    private ImageButton ib_location;
    private Context context;
    private Intent intent;
    private SharedPreferences sp;

    public WeatherFragment() {
        intent = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);
        initView(view);
        initEvent();
        weatherAdapter = new WeatherAdapter(getContext());
        lv_weather.setAdapter(weatherAdapter);

        ObjectInputStream ois = null;

        if(getActivity() != null){
            File weatherCache = FileUtils.getDisCacheDir(getActivity(),"WeatherBean");
            try{
                ois = new ObjectInputStream(new FileInputStream(weatherCache));
                WeatherBean wb = (WeatherBean) ois.readObject();

                weatherAdapter.setWeather(wb);

                tv_city.setText(wb.getName());
                tv_temp.setText(wb.getRealTemp() + "℃");

                tv_wd.setText(wb.getWindDir());
                tv_wp.setText(wb.getWindPow());

                tv_humdata.setText(wb.getHumidity());

                int aqi = wb.getAqi();

                if (aqi < 50) {
                    tv_airlevel.setText("优");
                } else if (aqi < 100) {
                    tv_airlevel.setText("良好");
                } else if (aqi < 150) {
                    tv_airlevel.setText("轻度");
                } else if (aqi < 200) {
                    tv_airlevel.setText("中度");
                } else if (aqi < 300) {
                    tv_airlevel.setText("高度");
                } else {
                    tv_airlevel.setText("严重");
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(ois != null){
                    try{
                        ois.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        //千万不要改，改了会爆炸
        try {
            if (sp.getString("weather code", "abc") != "abc") {
                //加载上一次选择地点的天气
                new WeatherRequest().execute(Urls.WEATHER + sp.getString("weather code", "abc"));
            } else {
                //默认获取昆山的天气
                new WeatherRequest().execute(Urls.WEATHER + "101190404");
            }
//            } else {
//                String weathercode = intent.getStringExtra("weather code");
//                if (weathercode != null) {
//                    new WeatherRequest().execute(Urls.WEATHER + intent.getStringExtra("weather code"));
//                } else {
//                    new WeatherRequest().execute(Urls.WEATHER + "101190404");
//                }
//            }

        } catch (Exception e) {
        }

        return view;
    }

    private void initView(View view) {
        //城市和温度
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_temp = (TextView) view.findViewById(R.id.tv_temp);
        //风向和风力
        tv_wd = (TextView) view.findViewById(R.id.tv_wd);
        tv_wp = (TextView) view.findViewById(R.id.tv_wp);
        //湿度和数据
        tv_hum = (TextView) view.findViewById(R.id.tv_hum);
        tv_humdata = (TextView) view.findViewById(R.id.tv_humdata);
        //空气质量
        tv_air = (TextView) view.findViewById(R.id.tv_air);
        tv_airlevel = (TextView) view.findViewById(R.id.tv_airlevel);

        lv_weather = (ListView) view.findViewById(R.id.lv_weather);

        //设置地区的按钮
        ib_location = (ImageButton) view.findViewById(R.id.ib_location);
    }

    public void initEvent() {
        ib_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ProvinceManage.class);
                startActivity(intent);
            }
        });
    }


    class WeatherRequest extends AsyncTask<String, Integer, WeatherBean> {

        private WeatherBean wb;
        private ObjectOutputStream oos;

        @Override
        protected WeatherBean doInBackground(String... params) {
            HttpUtils.getJsonString(params[0], new HttpUtils.HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    wb = JsonUtils.readJsonWeatherBean(response);
                    if(getActivity() != null){
                        File weatherCache = FileUtils.getDisCacheDir(getActivity(),"WeatherBean");
                        try{
                            oos = new ObjectOutputStream(new FileOutputStream(weatherCache));
                            oos.writeObject(wb);
                        }catch(Exception e){
                            e.printStackTrace();
                        }finally {
                            if(oos != null){
                                try{
                                    oos.close();
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
            return wb;
        }

        @Override
        protected void onPostExecute(WeatherBean weatherBean) {
            if (weatherBean == null) {
                Toast.makeText(getContext(), "获取数据失败！从缓存加载数据！", Toast.LENGTH_LONG).show();
            } else {
                tv_city.setText(weatherBean.getName());
                tv_temp.setText(weatherBean.getRealTemp() + "℃");

                tv_wd.setText(weatherBean.getWindDir());
                tv_wp.setText(weatherBean.getWindPow());

                tv_humdata.setText(weatherBean.getHumidity());

                int aqi = weatherBean.getAqi();

                if (aqi < 50) {
                    tv_airlevel.setText("优");
                } else if (aqi < 100) {
                    tv_airlevel.setText("良好");
                } else if (aqi < 150) {
                    tv_airlevel.setText("轻度");
                } else if (aqi < 200) {
                    tv_airlevel.setText("中度");
                } else if (aqi < 300) {
                    tv_airlevel.setText("高度");
                } else {
                    tv_airlevel.setText("严重");
                }
                weatherAdapter.setWeather(weatherBean);
            }
        }
    }

    public void onRefresh(String weathercode) {
        new WeatherRequest().execute(Urls.WEATHER + weathercode);
    }
}
