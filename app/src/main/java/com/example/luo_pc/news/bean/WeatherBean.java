package com.example.luo_pc.news.bean;

import java.io.Serializable;

/**
 * Created by luo-pc on 2016/6/20.
 */
public class WeatherBean implements Serializable{
    /*************************
     * forecast中的数据
     *************************/

    /**
     * 城市名
     */
    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 城市代码
     */
    private String weathercode;

    public String getWeathercode() {
        return weathercode;
    }

    public void setWeathercode(String weathercode) {
        this.weathercode = weathercode;
    }

    /**
     * 日期
     */
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 三天的天气
     */
    private String[] weather = new String[3];

    public String[] getWeather() {
        return weather;
    }

    public void setWeather(String[] weather) {
        this.weather = weather;
    }

    /********************
     * realtime中的数据
     ********************/

    /**
     * 湿度
     */
    private String humidity;

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * 风向
     */
    private String windDir;

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    /**
     * 风力
     */
    private String windPow;

    public String getWindPow() {
        return windPow;
    }

    public void setWindPow(String windPow) {
        this.windPow = windPow;
    }

    /**
     * 实时天气
     */
    private String realWeather;

    public String getRealWeather() {
        return realWeather;
    }

    public void setRealWeather(String realWeather) {
        this.realWeather = realWeather;
    }

    /**
     * 实时温度
     */
    private String realTemp;

    public String getRealTemp() {
        return realTemp;
    }

    public void setRealTemp(String realTemp) {
        this.realTemp = realTemp;
    }

    /*****************
     * alert中的数据
     *****************/
    private int aqi;

    public int getAqi() {
        return aqi;
    }

    public void setAqi(int aqi) {
        this.aqi = aqi;
    }

    private String air;
}
