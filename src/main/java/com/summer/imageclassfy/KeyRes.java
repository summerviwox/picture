package com.summer.imageclassfy;

import com.summer.base.bean.BaseBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyRes extends BaseBean {

    private String score;//置信度

    private String root;//识别结果的上层标签，有部分钱币、动漫、烟酒等tag无上层标签

    private String keyword;//图片中的物体或场景名称
}
