package com.ly.wk.mapper;

import com.ly.wk.modle.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TeacherMapper {
    @Insert("insert into teacher values(null,#{nickname},#{gender},#{phone},#{email},#{password},#{salt}" +
            ",#{name},#{ID},#{life_motto},#{work_years},#{work_position},#{image},#{create_time},#{state}" +
            ",#{fans},#{qq_openid},#{wx_openid},#{address})")
    int addTeacher(Teacher teacher);

    @Select("select tid,nickname,work_position,image " +
            "from teacher where tid=#{tid} and state=1")
    Teacher queryByTid(int tid);

    @Select("select tid,nickname,gender,life_motto,work_years,work_position,image,fans,address " +
            "from teacher where tid=#{tid} and state=1")
    Teacher findTeacherByTid(int tid);

    @Update("update teacher  set fans=fans+1 where tid=#{tid}")
    int addFans(int tid);

    @Update("update teacher set fans=fans-1 where tid=#{tid}")
    int removeFans(int tid);
}
