package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Alarm;
import java.util.List;

public interface AlarmMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Alarm record);

    Alarm selectByPrimaryKey(Integer id);

    List<Alarm> selectAll();

    int updateByPrimaryKey(Alarm record);
}