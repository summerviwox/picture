package com.summer.imageclassfy;

import com.summer.base.bean.BaseBean;
import com.summer.base.bean.BaseResBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenBean extends BaseBean {

    private String grant_type;

    private String client_id;

    private String client_secret;
}
