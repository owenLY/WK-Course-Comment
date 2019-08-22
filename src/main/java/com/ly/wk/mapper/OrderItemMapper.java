package com.ly.wk.mapper;

import com.ly.wk.modle.OrderItem;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderItemMapper {

    @Select("select iid,cid from order_item where oid=#{oid}")
    @Results({
            @Result(column = "cid",property = "course",
                one = @One(select = "com.ly.wk.mapper.CourseMapper.getByCid"))
    })
    List<OrderItem> getByOid(String oid);

    @Insert("insert into order_item values(null,#{cid},#{oid})")
    int addItem(int cid,String oid);
}
