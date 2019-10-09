package com.summer.mybatis.entity;

public class Welcome {
    private Integer id;

    private String url;

    private String data;

    private Integer checked;

    private String netpath;

    public String getNetpath() {
        return netpath;
    }

    public void setNetpath(String netpath) {
        this.netpath = netpath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }
}