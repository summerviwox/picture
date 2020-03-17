package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Welcome;

import java.util.List;

public interface WelcomeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Welcome record);

    Welcome selectByPrimaryKey(Integer id);

    List<Welcome> selectAll();

    int updateByPrimaryKey(Welcome record);
}