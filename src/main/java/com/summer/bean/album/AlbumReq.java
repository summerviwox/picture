package com.summer.bean.album;

import com.summer.mybatis.entity.Album;
import com.summer.mybatis.entity.Albumitem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@Setter
public class AlbumReq implements Serializable {

    private String parentid;

    private String name;

    private String detail;

    private ArrayList<String> albumItems;
}
