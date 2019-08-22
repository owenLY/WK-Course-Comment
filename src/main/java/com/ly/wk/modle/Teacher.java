package com.ly.wk.modle;

import lombok.Data;

import java.util.Date;

@Data
public class Teacher {
    private Integer tid;
    private String nickname; //昵称
    private Integer gender; // 1男 2女
    private String phone;
    private String email;
    private String password;
    private String salt;
    private String name; //真实姓名
    private String ID;
    private String life_motto;
    private Integer work_years;
    private String work_position;
    private String image;
    private Date create_time;
    private String address;
    private Integer state;
    private Integer fans;
    private String qq_openid;
    private String wx_openid;
}
