package com.ly.wk.mapper;

import com.ly.wk.modle.Video;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface VideoMapper {
    @Select("select vid,name from video  where lid=#{lid} order by sort")
    List<Video> queryByLid(int lid);

    @Select("select vid,name,url from video where vid=#{vid}")
    Video queryByVid(int vid);

    @Select("select vid,name,url,lid from video where vid=#{vid}")
    Video getByVid(int vid);
}
