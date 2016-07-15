package com.example.luo_pc.news.bean;

import java.io.Serializable;

/**
 * Created by luo-pc on 2016/6/13.
 */
public class Province implements Serializable{
    /**
     * 省id
     */
    private int id;
    /**
     * 省名
     */
    private String provinceName;
    /**
     * 省code
     */
    private String provinceCode;

    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getId() {
        return id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }
}
