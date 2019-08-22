package com.ly.wk.modle;

import lombok.Data;

import java.util.Date;

@Data
public class CourseComment {
    private Integer mid;
    private String content;
    private Date add_time;
    private Integer like_count;
    private Course course;
    private User user;
}
