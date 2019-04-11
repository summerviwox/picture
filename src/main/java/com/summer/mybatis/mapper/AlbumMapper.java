package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Album;
import java.util.List;

public interface AlbumMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Album record);

    Album selectByPrimaryKey(Integer id);

    List<Album> selectAll();

    int updateByPrimaryKey(Album record);
}