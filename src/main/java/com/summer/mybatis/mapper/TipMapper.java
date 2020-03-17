package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Tip;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface TipMapper {
    @Delete({
            "delete from tip",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
            "insert into tip (recordid, tipid, ",
            "ctime)",
            "values (#{recordid,jdbcType=INTEGER}, #{tipid,jdbcType=INTEGER}, ",
            "#{ctime,jdbcType=BIGINT})"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    int insert(Tip record);

    @Select({
            "select",
            "id, recordid, tipid, ctime",
            "from tip",
            "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "recordid", property = "recordid", jdbcType = JdbcType.INTEGER),
            @Result(column = "tipid", property = "tipid", jdbcType = JdbcType.INTEGER),
            @Result(column = "ctime", property = "ctime", jdbcType = JdbcType.BIGINT)
    })
    Tip selectByPrimaryKey(Integer id);

    @Select({
            "select",
            "id, recordid, tipid, ctime",
            "from tip"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "recordid", property = "recordid", jdbcType = JdbcType.INTEGER),
            @Result(column = "tipid", property = "tipid", jdbcType = JdbcType.INTEGER),
            @Result(column = "ctime", property = "ctime", jdbcType = JdbcType.BIGINT)
    })
    List<Tip> selectAll();

    @Update({
            "update tip",
            "set recordid = #{recordid,jdbcType=INTEGER},",
            "tipid = #{tipid,jdbcType=INTEGER},",
            "ctime = #{ctime,jdbcType=BIGINT}",
            "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Tip record);

    @Select({"select * from tip where recordid = #{recordid,jdbcType=INTEGER}"})
    List<Tip> selectTipsByRecordId(@Param("recordid") int recordid);

    @Select({"select * from tip where recordid =#{recordid,jdbcType=INTEGER} and tipid = #{tipid,jdbcType=INTEGER}"})
    List<Tip> isTipExist(@Param("recordid") int recordid, @Param("tipid") int tipid);

    @Select({"select * from tip where tipid = #{tipid,jdbcType=INTEGER}"})
    List<Tip> selectTipsByTipId(@Param("tipid") int tipid);

    @Select({"select * from tip where tipid = #{tipid,jdbcType=INTEGER} and  recordid = #{recordid,jdbcType=INTEGER}"})
    List<Tip> checkTipIsExist(@Param("recordid") int recordid, @Param("tipid") int tipid);
}