package com.ly.wk.service;


import com.ly.wk.modle.Course;
import com.ly.wk.modle.CourseType;
import com.ly.wk.modle.UserCourse;
import com.ly.wk.modle.Video;
import com.ly.wk.vo.Condition;
import com.ly.wk.vo.CourseListVo;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


public interface CourseService {
    CourseListVo queryCourseList();
    Course queryCourseByCid(int cid);
    Video queryVideoByVid(int vid);
    List<CourseType> queryAllCourseType();
    List<Course> queryCourseByCondition(Condition condition);
    int queryPageCount(Condition condition);
    int queryUserCourse(String uid,int cid);
    List<UserCourse> showMyCourse(String uid);
    List<Course> search(String key);
    Long findCurrentTime(String uid,int vid);
    void saveVideoSchedule(String uid,int vid,long currentTime);
    void joinStudy(ModelAndView mv,String uid,Integer cid);
}
