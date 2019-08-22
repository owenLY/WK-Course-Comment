package com.ly.wk.modle;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CourseType {
    private Integer tid;
    private String name;
    private Set<CourseType> childTypes=new HashSet<>();
}
