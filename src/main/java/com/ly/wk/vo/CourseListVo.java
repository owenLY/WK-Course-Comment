package com.ly.wk.vo;

import com.ly.wk.modle.Course;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CourseListVo implements Serializable {
    public static final long serialVersionUID=1L;

    private List<Course> freeList;
    private List<Course> newList;
    private List<Course> hotList;
    private List<Course> degreeList;
}
