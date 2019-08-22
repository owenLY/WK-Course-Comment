package com.ly.wk.mapper;

import com.ly.wk.modle.UserCourse;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface UserCourseMapper {
    @Insert("insert into user_course values(#{add_time},#{uid},#{cid})")
    int addUC(Date add_time,String uid,int cid);

    @Select("select count(*) from user_course where uid=#{uid} and cid=#{cid}")
    int get(String uid,int cid);

    @Select("select add_time,cid from user_course where uid=#{uid}")
    @Results({
            @Result(property = "course",column="cid",one = @One(select = "com.ly.wk.mapper.CourseMapper.getByCid"))
    })
    List<UserCourse> getMyCourse(String uid);
}
