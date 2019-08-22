package com.ly.wk.service;

import com.ly.wk.event.OrderTradeSuccessEvent;

public interface YunpianService {
    boolean sendMessage(String text,String mobile);
    void sendSuccessMessage(OrderTradeSuccessEvent event);
}
