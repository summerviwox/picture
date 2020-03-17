package com.summer.mybatis.entity;

public class Will {
    private Integer id;

    private String text;

    private Integer level;

    private Long ctime;

    private Long utime;

    private Long dtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public Long getDtime() {
        return dtime;
    }

    public void setDtime(Long dtime) {
        this.dtime = dtime;
    }
}