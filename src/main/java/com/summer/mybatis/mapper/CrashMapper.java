package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Crash;
import java.util.List;

public interface CrashMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Crash record);

    Crash selectByPrimaryKey(Integer id);

    List<Crash> selectAll();

    int updateByPrimaryKey(Crash record);
}