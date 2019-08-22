package com.ly.wk;

import org.springframework.context.ApplicationEvent;

public class HelloEvent extends ApplicationEvent {
    private String name;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public HelloEvent(Object source,String name) {
        super(source);
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
