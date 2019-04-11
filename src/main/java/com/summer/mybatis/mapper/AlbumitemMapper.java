package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Albumitem;
import java.util.List;

public interface AlbumitemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Albumitem record);

    Albumitem selectByPrimaryKey(Integer id);

    List<Albumitem> selectAll();

    int updateByPrimaryKey(Albumitem record);
}