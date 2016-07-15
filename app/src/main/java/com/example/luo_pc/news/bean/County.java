package com.example.luo_pc.news.bean;

import java.io.Serializable;

/**
 * Created by luo-pc on 2016/6/13.
 */
public class County implements Serializable{
    /**
     * 县、区id
     */
    private int id;
    /**
     * 县名
     */
    private String countyName;
    /**
     * 县code
     */
    private String countyCode;
    /**
     * 市id
     */
    private String cityId;
    /**
     * 请求天气数据code
     */
    private String weathercode;

    public void setId(int id) {
        this.id = id;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setWeathercode(String weathercode) {
        this.weathercode = weathercode;
    }

    public int getId() {
        return id;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public String getCityId() {
        return cityId;
    }

    public String getWeathercode() {
        return weathercode;
    }
}
