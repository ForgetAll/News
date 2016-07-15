package com.example.luo_pc.news.bean;

import java.io.Serializable;

/**
 * Created by luo-pc on 2016/7/6.
 */
public class ImageBean implements Serializable{
    /**
     * 关于图片的描述
     */
    private String desc;
    /**
     * 图片的url
     */
    private  String url;


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
