package com.summer.mybatis.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Record {

    public static final String ATYPE_VIDEO = "video";

    public static final String ATYPE_IMAGE = "image";

    public static final String ATYPE_TEXT = "text";

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

    private Integer ctype = 0;

    private String remark;

    private Integer parentid;


    public int isUploaded = 0;

    ArrayList<Tiplab> tiplabs;

    public static void setMyAType(Record record){
        switch (record.getAtype()){
            case "1":
                record.setAtype(Record.ATYPE_IMAGE);
                break;
            case "3":
                record.setAtype(Record.ATYPE_VIDEO);
                break;
        }
    }


}