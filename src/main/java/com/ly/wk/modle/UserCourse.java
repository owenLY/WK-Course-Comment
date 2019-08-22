package com.ly.wk.modle;

import lombok.Data;

import java.util.Date;

@Data
public class UserCourse {
    private Date add_time;
    private User user;
    private Course course;
}
