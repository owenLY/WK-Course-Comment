package com.ly.wk.modle;

import lombok.Data;

@Data
public class OrderItem {
    private  Integer iid;
    private  Course course;
    private  Order order;
}
