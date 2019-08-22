package com.ly.wk.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtils {
    private static ApplicationContext applicationContext;

    private SpringUtils(){}

    public static void setApplicationContext(ApplicationContext applicationContext){
        new SpringUtils().applicationContext=applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}
