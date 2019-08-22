package com.ly.wk.config;

import com.yunpian.sdk.YunpianClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:wk.properties")
public class YunpianConfig {

    @Value("${default.yunpian.apikey}")
    private String apikey;

    @Bean
    public YunpianClient yunpianClient(){
        return new YunpianClient(apikey);
    }
}
