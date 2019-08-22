package com.ly.wk.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@PropertySource("classpath:wk.properties")
public class DefaultParamBean {

    @Value("${default.imagePath}")
    private String imagePath;
}
