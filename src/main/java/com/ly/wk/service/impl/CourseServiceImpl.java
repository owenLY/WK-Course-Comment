package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.*;
import com.ly.wk.modle.*;
import com.ly.wk.service.CourseService;
import com.ly.wk.vo.Condition;
import com.ly.wk.vo.CourseListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class CourseServiceImpl implements CourseService {
    private Logger logger= LoggerFactory.getLogger(CourseServiceImpl.class);
    @Autowired private CourseMapper courseMapper;
    @Autowired private DefaultParamBean defaultParamBean;
    @Autowired private VideoMapper videoMapper;
    @Autowired private CourseTypeMapper courseTypeMapper;
    @Autowired private ValueOperations<String,Object> valueOperations;
    @Autowired private UserCourseMapper userCourseMapper;
    @Autowired private VideoScheduleMapper videoScheduleMapper;

    //根据不同条件查询不同版块的课程
    @Override
    public CourseListVo queryCourseList() {

        //先查看redis中是否存在
        CourseListVo courseListVo= (CourseListVo) valueOperations.get("courseListVo");
        if(courseListVo!=null){
            return courseListVo;
        }
        else
            courseListVo=new CourseListVo();

        Map<String,Object> condition=new HashMap<>(1);

        //查询最热的课程
        condition.put("sort","favorite_num");
        List<Course> hotList=courseMapper.queryByCondition(condition);
        addParentPath(hotList);
        courseListVo.setHotList(hotList);
        condition.clear();


        //查询最新课程
        condition.put("sort","add_time");
        List<Course> newList=courseMapper.queryByCondition(condition);
        addParentPath(newList);
        courseListVo.setNewList(newList);
        condition.clear();

        //查询难度为１的课程
        condition.put("degree",1);
        List<Course> degreeList=courseMapper.queryByCondition(condition);
        addParentPath(degreeList);
        courseListVo.setDegreeList(degreeList);
        condition.clear();


        //查询免费课程
        condition.put("price",0D);
        List<Course> freeList=courseMapper.queryByCondition(condition);
        addParentPath(freeList);
        courseListVo.setFreeList(freeList);

        //将推荐版块的课程信息缓存到redis
        valueOperations.set("courseListVo",courseListVo,60*60*24, TimeUnit.SECONDS);

        return courseListVo;
    }

    //根据课程cid查询
    @Override
    public Course queryCourseByCid(int cid) {
        Course course=null;
        try{
            course=courseMapper.query(cid);
            StringBuffer image=new StringBuffer(defaultParamBean.getImagePath());
            image.append(course.getTeacher().getImage());
            course.getTeacher().setImage(image.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return course;
    }

    //根据vid来查询
    @Override
    public Video queryVideoByVid(int vid) {
        Video video=null;
        try{
            video=videoMapper.queryByVid(vid);
            String url=video.getUrl();
            video.setUrl(defaultParamBean.getImagePath()+url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return video;
    }

    //查询所有的CourseType,包括父级类型和子级类型
    @Override
    public List<CourseType> queryAllCourseType() {
        List<CourseType> parentTypes=null;
        try{
            parentTypes=courseTypeMapper.queryAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return parentTypes;
    }

    @Override
    public List<Course> queryCourseByCondition(Condition condition) {
        List<Course> courses=null;
       try{
           courses=courseMapper.queryPage(condition);
           addParentPath(courses);
       }catch (Throwable t){
           t.printStackTrace();
       }
        return courses;
    }

    @Override
    public int queryPageCount(Condition condition) {
        return courseMapper.queryPageCount(condition);
    }

    @Override
    public int queryUserCourse(String uid, int cid) {
        return userCourseMapper.get(uid,cid);
    }

    @Override
    public List<UserCourse> showMyCourse(String uid) {
       List<UserCourse> list = userCourseMapper.getMyCourse(uid);
       list.stream().forEach(userCourse -> {
           userCourse.getCourse().setImage(defaultParamBean.getImagePath()+userCourse.getCourse().getImage());
       });
       return list;
    }

    @Override
    public List<Course> search(String key) {
        key="%"+key+"%";
        List<Course> courses=courseMapper.queryByKey(key);
        if(courses!=null) {
            courses.stream().forEach(course -> course.setImage(defaultParamBean.getImagePath()+course.getImage()));
            return courses;
        }
        else
            return new ArrayList<>();
    }

    @Override
    public Long findCurrentTime(String uid, int vid) {
        return videoScheduleMapper.get(uid,vid);
    }

    @Override
    public void saveVideoSchedule(String uid, int vid, long currentTime) {
        int flag=videoScheduleMapper.query(uid,vid);
        int result=0;
        if(flag==1){
            result=videoScheduleMapper.update(uid,vid,currentTime);
        }
        else {
            result=videoScheduleMapper.add(uid,vid,currentTime);
        }
    }

    @Override
    public void joinStudy(ModelAndView mv, String uid, Integer cid) {
        if(uid==null){
            Video video =courseMapper.getFirstVideo(cid);
            video.setUrl(defaultParamBean.getImagePath()+video.getUrl());
            mv.addObject("video",video);
        }
        else{
            Schedule schedule=videoScheduleMapper.getRecentByUid(uid);
            if(schedule!=null){
                Long currentTime=schedule.getCurrentTime();
                Video video=schedule.getVideo();
                video.setUrl(defaultParamBean.getImagePath()+video.getUrl());
                mv.addObject("video",video);
                mv.addObject("currentTime",currentTime.longValue());
            }
        }
        Course course=courseMapper.query(cid);
        mv.addObject("course",course);
    }

    //给图片，视频的相对路径添加父路径
    public void addParentPath(List<Course> courses){
        courses.stream().forEach((course -> {
            String image=course.getImage();
            StringBuffer path=new StringBuffer(defaultParamBean.getImagePath());
            path.append(image);
            course.setImage(path.toString());
        }));
    }


}
