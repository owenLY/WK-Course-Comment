package com.ly.wk.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:wk.properties")
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    @Value("${app_id}")
    public  String app_id;

    // 商户私钥，您的PKCS8格式RSA2私钥
    @Value("${merchant_private_key}")
    public  String merchant_private_key;

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    @Value("${alipay_public_key}")
    public  String alipay_public_key;

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${notify_url}")
    public  String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${return_url}")
    public  String return_url;

    // 签名方式
    @Value("${sign_type}")
    public  String sign_type;

    // 字符编码格式
    @Value("${charset}")
    public  String charset;

    // 支付宝网关
    @Value("${gatewayUrl}")
    public  String gatewayUrl;

    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(gatewayUrl,app_id,merchant_private_key,"json",charset, alipay_public_key,sign_type);
    }
}
