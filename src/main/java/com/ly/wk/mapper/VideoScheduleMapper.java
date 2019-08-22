package com.ly.wk.mapper;

import com.ly.wk.modle.Schedule;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface VideoScheduleMapper {
    @Select("select currentTime from video_schedule where uid=#{uid} and vid=#{vid}")
    Long get(String uid,int vid);

    @Select("select count(*) from video_schedule where uid=#{uid} and vid=#{vid}")
    int query(String uid,int vid);

    @Update("update video_schedule set currentTime=#{currentTime} where uid=#{uid} and vid=#{vid}")
    int update(String  uid,int vid,long currentTime);

    @Insert("insert into video_schedule values(null,#{currentTime},now(),#{uid},#{vid})")
    int add(String uid,int vid,long currentTime);

    @Select("select sid,currentTime,vid from video_schedule where uid=#{uid} order by createTime desc limit 1")
    @Results({
            @Result(column = "vid",property = "video",one = @One(select = "com.ly.wk.mapper.VideoMapper.queryByVid"))
    })
    Schedule getRecentByUid(String uid);
}
