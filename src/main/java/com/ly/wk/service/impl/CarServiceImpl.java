package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.CourseMapper;
import com.ly.wk.modle.Course;
import com.ly.wk.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    HashOperations<String,String,Object> hashOperations;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    private DefaultParamBean defaultParamBean;

    // car-uid

    @Override
    public String addCourse2Car(String uid, String cid) {
        if(uid!=null && cid!=null){
            Integer value= (Integer) hashOperations.get(uid,cid);
            if(value==null){
                hashOperations.put(uid,cid,1);
                return "添加成功";
            }
            else if(value==1)
                return "课程已在购物车";
        }
        return "添加失败";
    }

    @Override
    public List<Course> showCar(String uid) {
        if(uid==null)
            return null;

        Map<String,Object> map=hashOperations.entries(uid);
        System.err.println(map.size());
        Iterator<Map.Entry<String,Object>> it=map.entrySet().iterator();

        List<Course> courses=new ArrayList<>();
        while(it.hasNext()){
            String key=it.next().getKey();
            Integer value= (Integer) map.get(key);
            if(value==1){
                Integer cid=Integer.parseInt(key);
                Course course=courseMapper.getByCidOrder(cid);
                courses.add(course);
            }
        }

        courses.stream().forEach(course -> {
            course.setImage(defaultParamBean.getImagePath()+course.getImage());
        });

        return courses;
    }

    @Override
    public long removeItem(String uid, String...cids) {
        return hashOperations.delete(uid,cids);
    }
}
