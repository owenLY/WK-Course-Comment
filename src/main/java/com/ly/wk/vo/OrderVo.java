package com.ly.wk.vo;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class OrderVo {
    private String oid;
    private Double total_money;
    private Date create_time;
    private Integer state;
    private String uid;

    public OrderVo(){}

    public OrderVo(String oid, Double total_money, Date create_time, Integer state, String uid) {
        this.oid = oid;
        this.total_money = total_money;
        this.create_time = create_time;
        this.state = state;
        this.uid = uid;
    }
}
