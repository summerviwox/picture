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

    private String name;

    private ArrayList<String> albumItems;
}
