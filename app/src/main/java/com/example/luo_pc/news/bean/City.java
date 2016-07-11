package com.example.luo_pc.news.bean;

/**
 * Created by luo-pc on 2016/6/13.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
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
