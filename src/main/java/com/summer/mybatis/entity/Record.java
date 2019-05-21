package com.summer.mybatis.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Record {

    public static final String ATYPE_VIDEO = "video";

    public static final String ATYPE_IMAGE = "image";

    public static final String ATYPE_TEXT = "video";

    private Integer id;

    private String locpath;

    private String netpath;

    private Long ctime;

    private Long utime;

    private String atype;

    private String btype;

    private String address;

    private Long duration;

    private String name;

    private String content;

    private Integer enable;

    private Integer classify;//是否图片识别分类

    private Integer ctype;

    private Integer remark;

    private Integer parentid;


    public int isUploaded = 0;

    ArrayList<Tiplab> tiplabs;

}