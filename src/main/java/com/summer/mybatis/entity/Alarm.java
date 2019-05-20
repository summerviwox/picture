package com.summer.mybatis.entity;

public class Alarm implements Comparable<Alarm>{

    private Integer id;

    private Long time;

    private String text;

    private Integer enable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public int compareTo(Alarm o) {
        if(time%(1000*3600*24)>o.getTime()%(1000*3600*24)){
            return 1;
        }
        return -1;

    }
}