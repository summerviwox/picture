package com.summer.imageclassfy;

import com.summer.base.bean.BaseBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageClassifyBean extends BaseBean {

    private String access_token;

    private String image;

    private String baike_num;
}
