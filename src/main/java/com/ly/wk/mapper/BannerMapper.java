package com.ly.wk.mapper;

import com.ly.wk.modle.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BannerMapper {
    @Select("select bid,url from banner limit #{start},#{size}")
    List<Banner> query(int start, int size);
}
