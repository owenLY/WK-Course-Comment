package com.ly.wk.event;

import org.springframework.context.ApplicationEvent;

/**
 * 订单交易成功事件
 * 1、事件(通过继承ApplicationEvent)
 * ２、监听(实现ApplicationListener,也可以使用注解@EventListener)
 * ３、事件发布操作(ApplicationContext的publishEvent()方法)
 */
public class OrderTradeSuccessEvent extends ApplicationEvent {
    private String mobile;
    private String text;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public OrderTradeSuccessEvent(Object source,String mobile,String text) {
        super(source);
        this.mobile=mobile;
        this.text=text;
    }

    public String getMobile() {
        return mobile;
    }
}
