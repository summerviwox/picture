package com.summer.mybatis.mapper;

import com.summer.mybatis.entity.Record;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface RecordMapper {
    @Delete({
        "delete from record",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    int deleteByLocalPath(String locpath);

    @Insert({
        "insert into record (locpath, netpath, ",
        "ctime, utime, atype, ",
        "btype, address, ",
        "duration, name, content,classify)",
        "values (#{locpath,jdbcType=VARCHAR}, #{netpath,jdbcType=VARCHAR}, ",
        "#{ctime,jdbcType=BIGINT}, #{utime,jdbcType=BIGINT}, #{atype,jdbcType=VARCHAR}, ",
        "#{btype,jdbcType=VARCHAR}, #{address,jdbcType=VARCHAR}, ",
        "#{duration,jdbcType=BIGINT}, #{name,jdbcType=VARCHAR}, #{content,jdbcType=LONGVARCHAR},#{classify,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(Record record);

    @Select({
        "select",
        "id, locpath, netpath, ctime, utime, atype, btype, address, duration, name, content",
        "from record",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="locpath", property="locpath", jdbcType=JdbcType.VARCHAR),
        @Result(column="netpath", property="netpath", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.BIGINT),
        @Result(column="utime", property="utime", jdbcType=JdbcType.BIGINT),
        @Result(column="atype", property="atype", jdbcType=JdbcType.VARCHAR),
        @Result(column="btype", property="btype", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="duration", property="duration", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.LONGVARCHAR),
            @Result(column="classify", property="classify", jdbcType=JdbcType.INTEGER)
    })
    Record selectByPrimaryKey(Integer id);

    @Select({
        "select",
        "id, locpath, netpath, ctime, utime, atype, btype, address, duration, name, content",
        "from record"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="locpath", property="locpath", jdbcType=JdbcType.VARCHAR),
        @Result(column="netpath", property="netpath", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.BIGINT),
        @Result(column="utime", property="utime", jdbcType=JdbcType.BIGINT),
        @Result(column="atype", property="atype", jdbcType=JdbcType.VARCHAR),
        @Result(column="btype", property="btype", jdbcType=JdbcType.VARCHAR),
        @Result(column="address", property="address", jdbcType=JdbcType.VARCHAR),
        @Result(column="duration", property="duration", jdbcType=JdbcType.BIGINT),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.LONGVARCHAR),
            @Result(column="classify", property="classify", jdbcType=JdbcType.INTEGER)
    })
    List<Record> selectAll();

    @Select({"select count(id) from record where atype = #{atype,jdbcType=VARCHAR}"})
    int selectCount();

    @Select({ "select * from record where atype = #{atype,jdbcType=VARCHAR} order by ctime desc"})
    List<Record> selectAllByAtype(@Param("atype") String atype);

    @Select({ "select * from record where atype = #{atype,jdbcType=VARCHAR} and ctime>= #{start,jdbcType=VARCHAR} and ctime< #{end,jdbcType=VARCHAR} order by ctime desc"})
    List<Record> selectAllByAtypeWithSE(@Param("atype") String atype,@Param("start")String start,@Param("end")String end);

    @Select({ "select * from record where ctime>= #{start,jdbcType=VARCHAR} and ctime< #{end,jdbcType=VARCHAR} order by ctime desc"})
    List<Record> selectAllWithSE(@Param("start")String start,@Param("end")String end);

    @Select({ "select * from record where atype = #{atype,jdbcType=VARCHAR} limit 16 offset #{offset,jdbcType=INTEGER}"})
    List<Record> selectAllByAtypeStep(@Param("atype") String atype,@Param("offset") Integer offset);


    @Select({ "select * from record where atype = #{atype,jdbcType=VARCHAR} limit #{limit,jdbcType=INTEGER} offset #{offset,jdbcType=INTEGER}"})
    List<Record> selectAllByAtypeStepLimit(@Param("atype") String atype,@Param("limit") Integer limit,@Param("offset") Integer offset);

    @Update({
        "update record",
        "set locpath = #{locpath,jdbcType=VARCHAR},",
          "netpath = #{netpath,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=BIGINT},",
          "utime = #{utime,jdbcType=BIGINT},",
          "atype = #{atype,jdbcType=VARCHAR},",
          "btype = #{btype,jdbcType=VARCHAR},",
          "address = #{address,jdbcType=VARCHAR},",
          "duration = #{duration,jdbcType=BIGINT},",
          "name = #{name,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=LONGVARCHAR}",
            "classify = #{classify,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Record record);

    @Select({"select count(id) from record where locpath = #{locpath,jdbcType=VARCHAR}"})
    int  selectRecordNumWhereLocalPath(String locpath);
    @Select({"select count(id) from record where atype = #{atype,jdbcType=VARCHAR}"})
    int getRecordCount(@Param("atype") String atype);

    @Select({"select count(id) from record where atype = #{atype,jdbcType=VARCHAR} and ctime>= #{start,jdbcType=VARCHAR} and ctime< #{end,jdbcType=VARCHAR}"})
    int getRecordCountWithSE(@Param("atype") String atype,@Param("start")String start,@Param("end")String end);

    @Select({"select max(ctime) from record where atype = #{atype,jdbcType=VARCHAR}"})
    long getRecordMaxDate(@Param("atype") String atype);


    @Select({"select max(ctime) from record"})
    long getRecordMaxDateStamp();

    @Select({"select min(ctime) from record where atype = #{atype,jdbcType=VARCHAR}"})
    long getRecordMinDate(@Param("atype") String atype);


    @Select({"select min(ctime) from record"})
    long getRecordMinDateStamp();

    @Select({"select count(id) from record where netpath!='' and atype = #{atype,jdbcType=VARCHAR}"})
    int getUploadNum(@Param("atype") String atype);

    @Select({"select count(id) from record where netpath!='' and atype = #{atype,jdbcType=VARCHAR} and ctime>= #{start,jdbcType=VARCHAR} and ctime< #{end,jdbcType=VARCHAR}"})
    int getUploadNumWithSE(@Param("atype") String atype,@Param("start")String start,@Param("end")String end);


    @Select({"select * from record where locpath = #{locpath,jdbcType=VARCHAR} limit 1"})
    List<Record>  selectRecordWhereLocalPath(String locpath);

    @Select({"select id from record where locpath = #{locpath,jdbcType=VARCHAR} limit 1"})
    List<Record>  selectRecordIdWhereLocalPath(String locpath);

    @Update({"update record set netpath = #{netpath,jdbcType=VARCHAR} where locpath = #{locpath,jdbcType=VARCHAR}"})
    int updateNetPath(@Param("netpath") String netpath, @Param("locpath") String locpath);

    List<Record> selectAllByTypes(@Param("atype") String atype);

    List<Record> selectAllByTypesWithLimit(String atype,Integer start,Integer count);

    List<Record> selectAllNotImageCheckWithLimit(Integer count);

    List<Record> selectRecordsWithSizeLimit(Integer startid);

    List<Record> selectRecordsWithTypeSizeLimit(String atype,Integer startid);

    List<Record> selectRecordByLocalpath(String locpath);

    int updateClassify(Integer recordid,Integer value);

}