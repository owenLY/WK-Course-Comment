package com.ly.wk.mapper;

import com.ly.wk.modle.Order;
import com.ly.wk.vo.OrderVo;
import com.ly.wk.vo.QueryProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrderMapper {

    @SelectProvider(value = QueryProvider.class,method = "queryOrder")
    @Results({
            @Result(column = "oid",property = "oid"),
            @Result(column = "oid",property = "items",
                many = @Many(select = "com.ly.wk.mapper.OrderItemMapper.getByOid"))
    })
    List<Order> getByUid(String uid,int state);

    @Insert("insert into _order values(#{oid},#{total_money},#{create_time},#{state},#{uid},null)")
    int addOrder(OrderVo orderVo);

    @Select("select oid,total_money,create_time,state,uid from _order where oid=#{oid}")
    @Results({
            @Result(column = "oid",property = "items",
                    many = @Many(select = "com.ly.wk.mapper.OrderItemMapper.getByOid")),
            @Result(column = "uid",property = "user",
                    one=@One(select = "com.ly.wk.mapper.UserMapper.get"))
    })
    Order getByOid(String oid);


    @Update("update  _order set state=#{state},alipay_trade_no=#{alipay_trade_no} where oid=#{oid}")
    int updateStateAndAlipay(String oid,int state,String alipay_trade_no);

    @Select("select state from _order where oid=#{oid}")
    int findState(String oid);

    @Select("select uid from _order where oid=#{oid}")
    String getUid(String oid);
}
