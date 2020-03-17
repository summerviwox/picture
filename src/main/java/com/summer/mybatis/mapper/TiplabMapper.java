package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Tiplab;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface TiplabMapper {
    @Delete({
            "delete from tiplab",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    int deleteByContent(String content);

    @Insert({
            "insert into tiplab (content, enable, ",
            "ctime)",
            "values (#{content,jdbcType=VARCHAR}, #{enable,jdbcType=INTEGER}, ",
            "#{ctime,jdbcType=BIGINT})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int insert(Tiplab record);

    @Select({
            "select",
            "id, content, enable, ctime",
            "from tiplab",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "content", property = "content", jdbcType = JdbcType.VARCHAR),
            @Result(column = "enable", property = "enable", jdbcType = JdbcType.INTEGER),
            @Result(column = "ctime", property = "ctime", jdbcType = JdbcType.BIGINT)
    })
    Tiplab selectByPrimaryKey(Integer id);

    @Select({
            "select",
            "id, content, enable, ctime",
            "from tiplab"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "content", property = "content", jdbcType = JdbcType.VARCHAR),
            @Result(column = "enable", property = "enable", jdbcType = JdbcType.INTEGER),
            @Result(column = "ctime", property = "ctime", jdbcType = JdbcType.BIGINT)
    })
    List<Tiplab> selectAll();

    @Update({
            "update tiplab",
            "set content = #{content,jdbcType=VARCHAR},",
            "enable = #{enable,jdbcType=INTEGER},",
            "ctime = #{ctime,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Tiplab record);

    @Select({"select * from tiplab where content = #{content,jdbcType = VARCHAR}"})
    List<Tiplab> selectTipLabByContent(@Param("content") String content);

    @Select({"select * from tiplab where content like concat('%',#{content,jdbcType = VARCHAR},'%')"})
    List<Tiplab> selectLikeTipLabByContent(@Param("content") String content);

    List<Tiplab> selectTipLabwithLimit(int limit);
}