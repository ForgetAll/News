package com.example.luo_pc.news.bean;

import java.io.Serializable;

/**
 * Created by luo-pc on 2016/6/13.
 */
public class City implements Serializable{
    /**
     * 城市id
     */
    private int id;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 城市code
     */
    private String cityCode;
    /**
     * 省id
     */
    private String provinceId;

    public void setId(int id) {
        this.id = id;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getProvinceId() {
        return provinceId;
    }
}
