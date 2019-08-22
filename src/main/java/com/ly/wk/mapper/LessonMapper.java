package com.ly.wk.mapper;

import com.ly.wk.modle.Lesson;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LessonMapper {

    @Select("select lid,name from lesson where cid=#{cid} order by add_time desc")
    @Results(value={
            @Result(column = "lid",property = "lid"),
            @Result(column = "name",property = "name"),
            @Result(column = "lid",property = "videos",many = @Many(select = "com.ly.wk.mapper.VideoMapper.queryByLid"))
    })
    List<Lesson> queryByCid(int cid);

}
