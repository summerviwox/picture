package com.summer.base.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by SWSD on 2017-07-28.
 */
@Getter
@Setter
public class BaseResBean extends BaseBean {

    private Object data;

    private Object other;

    private boolean isException = false;

    private int errorCode = -1;

    private String errorMessage ="";

    private int total;
}
