package com.ly.wk;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class HelloEventListener implements ApplicationListener<HelloEvent> {

    @Override
    public void onApplicationEvent(HelloEvent event) {
        System.out.println(event.getName());
    }
}
