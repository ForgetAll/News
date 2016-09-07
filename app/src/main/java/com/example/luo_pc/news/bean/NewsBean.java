package com.example.luo_pc.news.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by luo-pc on 2016/5/13.
 */
public class NewsBean implements Serializable {
    /**
     * docid
     */
    private String docid;
    /**
     * 标题
     */
    private String title;
    /**
     * 小内容
     */
    private String digest;
    /**
     * 图片地址
     */
    private String imgsrc;
    /**
     * 来源
     */
    private String source;
    /**
     * 时间
     */
    private String ptime;
    /**
     * TAG
     */
    private String tag;

    /**
     * url
     */
    private String url_3w;

    @SuppressWarnings("unused")
    public String getDocid() {
        return docid;
    }

    @SuppressWarnings("unused")
    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    @SuppressWarnings("unused")
    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    @SuppressWarnings("unused")
    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @SuppressWarnings("unused")
    public String getPtime() {
        return ptime;
    }

    @SuppressWarnings("unused")
    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl_3w() {
        return url_3w;
    }

    @SuppressWarnings("unused")
    public void setUrl_3w(String url_3w) {
        this.url_3w = url_3w;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NewsBean)
            return ((NewsBean) o).url_3w.equals(url_3w);
        return false;
    }

    @Override
    public int hashCode() {
        return 13 + this.url_3w.hashCode() + this.getDocid().hashCode();
    }
}
