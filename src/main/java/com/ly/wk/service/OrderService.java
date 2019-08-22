package com.ly.wk.service;

import com.ly.wk.modle.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Order submitOrder(String uid,Integer[] cids);

    String prePay(String oid);

    String dealOrderResult(Map<String,String> params);

    boolean verifyOrderResult(Map<String,String> params);

    List<Order> queryOrder(String uid,int state);
}
