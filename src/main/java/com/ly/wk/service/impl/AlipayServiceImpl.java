package com.ly.wk.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.ly.wk.config.AlipayConfig;
import com.ly.wk.service.AlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private AlipayConfig alipayConfig;

    @Override
    public String alipay(String oid,double total_amount, String subject) {
        //AlipayTradePrecreateRequest request=new AlipayTradePrecreateRequest();
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        request.setBizContent("{" +
                "\"out_trade_no\":\""+oid+"\"," +    //商户订单号
                "\"total_amount\":\""+total_amount+"\"," +
                "\"subject\":\""+subject+"\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");



        request.setNotifyUrl(alipayConfig.notify_url);
        request.setReturnUrl(alipayConfig.return_url);
        try {
            System.err.println(alipayClient);
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if(response.isSuccess()) {
                System.err.println("success");
                return response.getBody();
            }
            else
                System.err.println("failure");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean signVerified(Map<String, String> params, String alipay_public_key, String charset, String sign_type) {
        try {
            return AlipaySignature.rsaCheckV1(params,alipay_public_key,charset,sign_type);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return false;
    }
}
