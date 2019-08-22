package com.ly.wk.modle;

import lombok.Data;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class Lesson {
    private Integer lid;
    private String name;
    private Date addTime;

    private Course course;
    private Set<Video> videos=new HashSet<>();
}
