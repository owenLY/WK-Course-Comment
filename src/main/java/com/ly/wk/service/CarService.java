package com.ly.wk.service;

import com.ly.wk.modle.Course;

import java.util.List;

public interface CarService {
    String addCourse2Car(String uid,String cid);
    List<Course> showCar(String uid);
    long removeItem(String uid,String...cids);
}
