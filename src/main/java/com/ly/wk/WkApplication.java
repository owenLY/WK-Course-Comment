package com.ly.wk;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
public class WkApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
      SpringApplication.run(WkApplication.class, args);

    }

    /**
     * 用于Springboot使用外部tomcat
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WkApplication.class);
    }

}
