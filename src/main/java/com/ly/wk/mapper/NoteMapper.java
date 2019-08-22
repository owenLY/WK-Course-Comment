package com.ly.wk.mapper;

import com.ly.wk.modle.Note;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.List;

@Mapper
@Repository
public interface NoteMapper {
    @Select("select nid,content,create_time,uid from note where cid=#{cid}")
    @Results({
            @Result(column = "uid",property = "user",
            one = @One(select = "com.ly.wk.mapper.UserMapper.get"))
    })
    List<Note> getByCid(int cid);
}
