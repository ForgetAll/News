package com.example.luo_pc.news.utils;

import android.util.Log;

import com.example.luo_pc.news.bean.ImageBean;
import com.example.luo_pc.news.bean.NewsBean;
import com.example.luo_pc.news.bean.WeatherBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luo-pc on 2016/5/26.
 */
public class JsonUtils {
    static final String TAG = "JsonUtils";
    /**
     * 将获取到的json转换为新闻列表对象
     *
     * @param res
     * @param value
     * @return
     */
    public static ArrayList<NewsBean> readJsonNewsBean(String res, String value) {

        ArrayList<NewsBean> beans = new ArrayList<NewsBean>();
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(res).getAsJsonObject();
            JsonElement jsonElement = jsonObj.get(value);
            if (jsonElement == null) {
                return null;
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (int i = 1; i < jsonArray.size(); i++) {
                JsonObject jo = jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }


                if (!jo.has("imgextra") && jo.has("url")) {
                    NewsBean news = GsonUtils.deserialize(jo, NewsBean.class);
                    beans.add(news);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return beans;
    }

    /**
     * 因为weather接口返回的字段比较奇怪，所以采用Android自带的Json工具解析
     */
    public static WeatherBean readJsonWeatherBean(String response) {
        WeatherBean weatherBean = new WeatherBean();
        try {
            JSONObject root = new JSONObject(response);
            JSONObject forecast = root.getJSONObject("forecast");
            //从forecast得到数据
            weatherBean.setName(forecast.getString("city"));
            weatherBean.setWeathercode(forecast.getString("cityid"));
            weatherBean.setDate(forecast.getString("date_y"));
            String[] weather = new String[3];
            weather[0] = forecast.getString("weather1");
            weather[1] = forecast.getString("weather2");
            weather[2] = forecast.getString("weather3");

            weatherBean.setWeather(weather);

            //从realtime中获取数据
            JSONObject realtime = root.getJSONObject("realtime");

            weatherBean.setHumidity(realtime.getString("SD"));
            weatherBean.setWindDir(realtime.getString("WD"));
            weatherBean.setWindPow(realtime.getString("WS"));
            weatherBean.setRealTemp(realtime.getString("temp"));
            weatherBean.setRealWeather(realtime.getString("weather"));

            //从alert中获取数据
            JSONObject aqi = root.getJSONObject("aqi");

            weatherBean.setAqi(aqi.getInt("aqi"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherBean;
    }

    public static ArrayList<ImageBean> readJsonImageBean(String response) {
        ArrayList<ImageBean> imageList = new ArrayList<ImageBean>();
        try {

            JSONObject root = new JSONObject(response);
            if (!root.getBoolean("error")) {
                JSONArray results = root.getJSONArray("results");
                if(results.length() > 0) {
                    for (int i = 0; i < results.length(); i++) {
                        ImageBean imageBean = new ImageBean();
                        JSONObject jo = (JSONObject) results.get(i);
                        imageBean.setDesc(jo.getString("desc"));
                        imageBean.setUrl(jo.getString("url"));
                        imageList.add(imageBean);
                    }
                    return imageList;
                }else{
                    Log.i(TAG,"helloworld");
                    return null;
                }

            } else
                return null;

        } catch (Exception e) {

        }
        return null;
    }

}
