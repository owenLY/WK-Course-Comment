package com.ly.wk.mapper;

import com.ly.wk.modle.User;
import com.ly.wk.vo.PersonalDataVo;
import com.ly.wk.vo.QueryProvider;
import com.ly.wk.vo.UserVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    @Insert("insert into user(uid,username,password,salt,state,email,mobile,code,createTime) " +
            "values(#{uid},#{username},#{password},#{salt},#{state},#{email},#{mobile},#{code},#{createTime})")
    int addUser(UserVo userVo);

    @Select("select uid,state,nickname,avatar,birth,gender,address,mobile,email,createTime " +
            "from user where username=#{username} and password=#{password} and state=1")
    User queryUser(User user);

    @Select("select salt from user where username=#{username}")
    String querySalt(String username);

    @Select("select uid,nickname,avatar from user where uid=#{uid}")
    User get(String uid);

    @Select("select count(*) from user where username=#{username}")
    int getByUsername(String username);

    @Select("select count(*) from user where email=#{email}")
    int getByEmail(String email);

    @Select("select count(*) from user where mobile=#{mobile}")
    int getByMobile(String mobile);

    @Select("select mobile from user where uid=#{uid}")
    String getMobile(String uid);

    @UpdateProvider(type = QueryProvider.class,method = "updateUser")
    int update(PersonalDataVo personalDataVo);

    @Select("select nickname,avatar,birth,gender,address from user where uid=#{uid}")
    PersonalDataVo queryByUid(String uid);
}
