package com.summer.imageclassfy;

import com.summer.base.bean.BaseBean;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class ImageClassifyRes extends BaseBean {

    private String log_id;

    private Integer result_num;

    private ArrayList<KeyRes> result;

    private String error_code;


}
