package com.ly.wk.mapper;

import com.ly.wk.modle.CourseComment;
import com.ly.wk.vo.CommentVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    @Select("select mid,content,add_time,uid from course_comment where cid=#{cid} order by add_time desc")
    @Results({
            @Result(column ="uid",property = "user",
                one=@One(select = "com.ly.wk.mapper.UserMapper.get"))
    })
    List<CourseComment> getByCid(int cid);

    @Insert("insert into course_comment values(null,#{content},#{add_time},#{cid},#{uid})")
    @Options(useGeneratedKeys = true,keyProperty = "mid",keyColumn = "mid")
    int add(CommentVo commentVo);
}
