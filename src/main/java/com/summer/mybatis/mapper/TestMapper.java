package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Record;

import java.util.List;

public interface TestMapper {

    public List<Record> selectSome(int tipid);
}
