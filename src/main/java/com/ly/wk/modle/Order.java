package com.ly.wk.modle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class Order {
    private String oid;
    private Double total_money;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date create_time;
    private Integer state;
    private String alipay_trade_no; //支付宝交易号
    private User user;

    private Set<OrderItem> items=new HashSet<>();
}
