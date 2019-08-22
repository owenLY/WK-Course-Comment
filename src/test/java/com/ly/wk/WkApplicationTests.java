package com.ly.wk;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.*;
import com.ly.wk.modle.*;
import com.ly.wk.service.OrderService;
import com.ly.wk.utils.Md5Util;
import com.ly.wk.utils.SpringUtils;
import com.ly.wk.utils.UUidUtil;
import com.ly.wk.vo.Condition;
import com.ly.wk.vo.CourseListVo;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.YunpianConf;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WkApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    DefaultParamBean defaultParamBean;
    @Test
    public void contextLoads() {
        System.out.println(defaultParamBean.getImagePath());
    }

    @Test
    public void testAddUser(){
    }
    @Autowired
    CourseMapper courseMapper;
    @Test
    public void testCourseMapper(){
        Map<String,Object> condition=new HashMap<>();
        condition.put("sort","add_time");
        List<Course> list=courseMapper.queryByCondition(condition);
        System.out.println(list);
    }

    @Test
    public void testCascadeQuery(){
        Course course=courseMapper.query(11);
        System.out.println(course);
    }

    @Autowired private CourseTypeMapper courseTypeMapper;

    @Test
    public void testCourseTypeMapper(){
        List<CourseType> list=courseTypeMapper.queryAll();
        System.err.println(list);
    }

    @Test
    public void testQueryPage(){
        Condition condition=new Condition();
        condition.setTid(14);
        condition.setStart(1);
        condition.setSize(2);
        List<Course> courses=courseMapper.queryPage(condition);
        System.out.println(courses);
    }

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    public void testAddTeacher(){
        Teacher teacher=new Teacher();
        teacher.setNickname("技术胖");
        teacher.setGender(1);
        String salt=Md5Util.getSalt();
        teacher.setSalt(salt);
        teacher.setPassword(Md5Util.digest("123",salt));
        teacher.setLife_motto("时间就像海绵里的水,挤一挤总会有的.");
        teacher.setWork_years(8);
        teacher.setWork_position("Java开发工程师");
        teacher.setCreate_time(new Date(System.currentTimeMillis()));
        teacher.setState(1);
        teacher.setFans(2334);
        teacher.setAddress("北京东城");
        teacherMapper.addTeacher(teacher);
    }

    @Autowired private CommentMapper commentMapper;
    @Test
    public void testCommentMapper(){
        List<CourseComment> comments=commentMapper.getByCid(13);
        System.out.println(comments);
    }

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    ValueOperations<String,Object> valueOperations;

    @Test
    public void testRedisTemplate(){
//        String name="小罗";
//        redisTemplate.opsForValue().set("name",name);
//        String value= (String) redisTemplate.opsForValue().get(name);
//        valueOperations.set("name",name);
//        System.err.println(valueOperations.get("name"));
        
        CourseListVo courseListVo= (CourseListVo) valueOperations.get("courseListVo");
        System.err.println(courseListVo);
    }

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderService orderService;
    @Test
    public void testIN(){
        List<Order> orders=orderService.queryOrder("5dd6bad027d640208adc0ed0a440d724",0);
        System.err.println(orders);
    }

    @Test
    public void testSearch(){
        List<Course> courses=courseMapper.queryByKey("%java%");
        System.out.println(courses);
    }

    @Autowired
    YunpianClient client;

    @Test
    public void  testYunPian(){
        Map<String,String> param =client.newParam(2);
        param.put(YunpianConf.MOBILE,"17373421243");
        int code=1234;
        param.put(YunpianConf.TEXT,"【云片网】您的验证码是"+code);
        Result<SmsSingleSend> r = client.sms().single_send(param);
        System.out.println(r);
         client.close();
    }


    @Test
    public void test(){
        SpringUtils.getApplicationContext().publishEvent(new HelloEvent(this,"监听事件"));
    }
}