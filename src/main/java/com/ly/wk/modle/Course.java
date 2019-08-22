package com.ly.wk.modle;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class Course {
    private Integer cid;
    private String name;
    private String desc;    //课程简介
    private Integer degree; //难度　1初级　2中级  3高级
    private Double price;   //价格
    private Long duration;  //时长
    private Integer studentNum; //学生人数
    private Integer favoriteNum;    //收藏的人数
    private String image;   //图片路径
    private Date addTime;   //添加时间

    private CourseType type; //类别
    private Teacher teacher;
    private Set<Lesson> lessons=new HashSet<>();
}
