package com.ly.wk.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FollowMapper {

    @Insert("insert into teacher_follower values(null,#{uid},#{tid},now())")
    int add(String uid,int tid);

    @Select("select count(*) from teacher_follower where uid=#{uid} and tid=#{tid}")
    int find(String uid,int tid);

    @Update("delete from teacher_follower where uid=#{uid} and tid=#{tid}")
    int remove(String uid,int tid);
}
