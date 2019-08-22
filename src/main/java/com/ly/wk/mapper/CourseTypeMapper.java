package com.ly.wk.mapper;

import com.ly.wk.modle.CourseType;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CourseTypeMapper {
    @Select("select tid,name from course_type where parent is NULL")
    @Results(value={
            @Result(property = "tid",column = "tid",id = true),
            @Result(property = "name",column = "name"),
            @Result(column = "tid",property = "childTypes"
                    ,many =@Many(select = "com.ly.wk.mapper.CourseTypeMapper.queryByParent"))
    })
    List<CourseType> queryAll();

    @Select("select tid,name from course_type where parent=#{tid}")
    List<CourseType> queryByParent(int tid);
}
