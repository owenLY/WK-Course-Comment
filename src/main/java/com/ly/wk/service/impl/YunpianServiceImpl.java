package com.ly.wk.service.impl;

import com.ly.wk.event.OrderTradeSuccessEvent;
import com.ly.wk.service.YunpianService;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.YunpianConf;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class YunpianServiceImpl implements YunpianService {
    private Logger logger= LoggerFactory.getLogger(YunpianServiceImpl.class);
    @Autowired
    YunpianClient client;

    @Override
    public boolean sendMessage(String text, String mobile) {
        client.init();
        Map<String,String> param=client.newParam(2);
        param.put(YunpianConf.MOBILE,mobile);
        param.put(YunpianConf.TEXT,text);
        Result<SmsSingleSend> r = client.sms().single_send(param);
        logger.info(r.toString());
        int code=r.getCode();
        return code==0 ? true : false;
    }

    @EventListener(OrderTradeSuccessEvent.class)
    @Override
    public void sendSuccessMessage(OrderTradeSuccessEvent event) {
        client.init();
        Map<String,String> param=client.newParam(2);
        param.put(YunpianConf.MOBILE,event.getMobile());
        param.put(YunpianConf.TEXT,event.getMobile());
        Result<SmsSingleSend> r = client.sms().single_send(param);
        logger.info(r.toString());
    }

}
