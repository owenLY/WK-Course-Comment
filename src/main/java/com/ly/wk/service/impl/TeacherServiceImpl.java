package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.TeacherMapper;
import com.ly.wk.modle.Teacher;
import com.ly.wk.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired private TeacherMapper teacherMapper;
    @Autowired private DefaultParamBean defaultParamBean;

    @Override
    public Teacher findByTid(int tid) {
        Teacher teacher=null;
        try{
            teacher = teacherMapper.findTeacherByTid(tid);
            StringBuffer image=new StringBuffer(defaultParamBean.getImagePath());
            image.append(teacher.getImage());
            teacher.setImage(image.toString());
        }catch (Throwable t){
            t.printStackTrace();
        }
        return teacher;
    }
}
