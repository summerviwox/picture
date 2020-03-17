package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Will;

import java.util.List;

public interface WillMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Will record);

    Will selectByPrimaryKey(Integer id);

    List<Will> selectAll(Integer level);

    List<Will> selectAllNotDelete();

    int updateByPrimaryKey(Will record);

    int updateLevelByPrimaryKey(Integer id, Integer level);
}