package com.example.luo_pc.news.utils;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luo_pc.news.R;


/**
 * Created by luo-pc on 2016/6/26.
 */
public class WeatherImageUtils {

    public static boolean setImage(ImageView imageView, TextView textView) {
        String text = null;
        if (!TextUtils.isEmpty(textView.getText())) {
            text = (String) textView.getText();
        } else
            return false;
        if (text.contains("转")) {
            String[] weather = text.split("转");
            if (weather[0].equals("晴")) {
                imageView.setImageResource(R.drawable.weather_sun);
            } else if (weather[0].equals("雷阵雨")) {
                imageView.setImageResource(R.drawable.weather_thundershower);
            } else if (weather[0].equals("多云")) {
                imageView.setImageResource(R.drawable.weather_cloudy);
            } else if (weather[0].equals("阴")) {
                imageView.setImageResource(R.drawable.weather_overcast);
            } else if (weather[0].equals("小雨")) {
                imageView.setImageResource(R.drawable.weather_lightrain);
            } else if (weather[0].equals("中雨")) {
                imageView.setImageResource(R.drawable.weather_moderaterain);
            } else if (weather[0].equals("大雨")) {
                imageView.setImageResource(R.drawable.weather_heavyrain);
            } else if (weather[0].equals("暴雨")) {
                imageView.setImageResource(R.drawable.weather_storm);
            } else if (weather[0].equals("大暴雨")) {
                imageView.setImageResource(R.drawable.weather_heavystorm);
            } else if (weather[0].equals("小雪")) {
                imageView.setImageResource(R.drawable.weather_lightsnow);
            } else if (weather[0].equals("中雪")) {
                imageView.setImageResource(R.drawable.weather_moderatesnow);
            } else if (weather[0].equals(("大雪"))) {
                imageView.setImageResource(R.drawable.weather_heavysnow);
            } else if (weather[0].equals("暴雪")) {
                imageView.setImageResource(R.drawable.weather_snowstorm);
            } else if (weather[0].equals("阵雨")) {
                imageView.setImageResource(R.drawable.weather_showerrain);
            } else if (weather[0].equals("阵雪")) {
                imageView.setImageResource(R.drawable.weather_showersnow);
            }else {
                imageView.setImageResource(R.drawable.weather_unknow);
            }
        } else {
            if (text.equals("晴")) {
                imageView.setImageResource(R.drawable.weather_sun);
            } else if (text.equals("雷阵雨")) {
                imageView.setImageResource(R.drawable.weather_thundershower);
            } else if (text.equals("多云")) {
                imageView.setImageResource(R.drawable.weather_cloudy);
            } else if (text.equals("阴")) {
                imageView.setImageResource(R.drawable.weather_overcast);
            } else if (text.equals("小雨")) {
                imageView.setImageResource(R.drawable.weather_lightrain);
            } else if (text.equals("中雨")) {
                imageView.setImageResource(R.drawable.weather_moderaterain);
            } else if (text.equals("大雨")) {
                imageView.setImageResource(R.drawable.weather_heavyrain);
            } else if (text.equals("暴雨")) {
                imageView.setImageResource(R.drawable.weather_storm);
            } else if (text.equals("大暴雨")) {
                imageView.setImageResource(R.drawable.weather_heavystorm);
            } else if (text.equals("小雪")) {
                imageView.setImageResource(R.drawable.weather_lightsnow);
            } else if (text.equals("中雪")) {
                imageView.setImageResource(R.drawable.weather_moderatesnow);
            } else if (text.equals(("大雪"))) {
                imageView.setImageResource(R.drawable.weather_heavysnow);
            } else if (text.equals("暴雪")) {
                imageView.setImageResource(R.drawable.weather_snowstorm);
            } else if (text.equals("冰雹")) {
                imageView.setImageResource(R.drawable.weather_hail);
            } else if (text.equals("阵雪")) {
                imageView.setImageResource(R.drawable.weather_snowflurry);
            } else if (text.equals("雨夹雪")) {
                imageView.setImageResource(R.drawable.weather_sleet);
            } else if (text.equals("阵雨")) {
                imageView.setImageResource(R.drawable.weather_showerrain);
            }else if (text.equals("阵雪")) {
                imageView.setImageResource(R.drawable.weather_showersnow);
            }else {
                imageView.setImageResource(R.drawable.weather_sleet);
            }
        }

        return true;
    }

}
