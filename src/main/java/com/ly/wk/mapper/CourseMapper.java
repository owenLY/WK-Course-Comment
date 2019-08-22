package com.ly.wk.mapper;

import com.ly.wk.modle.Course;
import com.ly.wk.modle.Video;
import com.ly.wk.vo.Condition;
import com.ly.wk.vo.QueryProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface CourseMapper {

    @SelectProvider(type= QueryProvider.class,method = "queryByCondition")
    List<Course> queryByCondition(Map<String,Object> condition);



    /**
     *  mybatis注解式级联查询
     * @param cid
     * @return
     */

    @Select("select cid,name,course_desc,price,degree,duration,teacher_id from course where cid=#{cid}")
    @Results(value={
            @Result(column = "cid",property = "cid",id=true),
            @Result(column = "name",property = "name"),
            @Result(column = "course_desc",property = "desc"),
            @Result(column = "price",property = "price"),
            @Result(column = "degree",property = "degree"),
            @Result(column = "duration",property = "duration"),
            /**
             *  一对多查询
             *  "cid"作为com.ly.wk.mapper.LessonMapper.queryByCid的查询参数
             *  "lessons"是将返回的结果集存入lessons中
             */
            @Result(column = "cid",property = "lessons",
                    many = @Many(select = "com.ly.wk.mapper.LessonMapper.queryByCid")),
            @Result(column = "teacher_id",property = "teacher",
                    one = @One(select = "com.ly.wk.mapper.TeacherMapper.queryByTid"))
    })
    Course query(Integer cid);

    @SelectProvider(type = QueryProvider.class,method = "queryPage")
    List<Course> queryPage(Condition condition);

    @SelectProvider(type=QueryProvider.class,method = "queryPageCount")
    int queryPageCount(Condition condition);

    @Select("select cid,name,price,image from course where cid=#{cid}")
    Course getByCid(int cid);

    @Select("select cid,name,price,image from course where cid=#{cid}")
    Course getByCidOrder(int cid);

    @Select("select round(sum(price),2) from course where cid in (${cids})")
    Double getPriceSum(String cids);

    @Select("select cid,name,course_desc,price,image from course where name like #{key}")
    @Results({
            @Result(column = "course_desc",property = "desc")
    })
    List<Course> queryByKey(String key);

    @Select("select vid,url from video where lid " +
            "in(select lid from course c join lesson l on c.cid=l.cid where c.cid=#{cid}) " +
            "order by sort limit 1")
    Video getFirstVideo(int cid);
}
