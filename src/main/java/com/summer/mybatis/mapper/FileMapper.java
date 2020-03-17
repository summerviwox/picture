package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.File;

import java.util.List;

public interface FileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(File record);

    File selectByPrimaryKey(Integer id);

    List<File> selectAll();

    List<File> selectAllFolder();

    List<File> selectAllByParentId(Integer parentId);

    int updateByPrimaryKey(File record);

    int updateHeadIdByPrivateKey(Integer id, Integer headid);

    int updateParentByPrivateKey(Integer id, Integer parentId);

}