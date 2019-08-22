package com.ly.wk.modle;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private String uid;
    private String username;
    private String password;    //加密后的密码
    private String salt;    //盐
    private int state;  //  1、可用  -1、禁用
    private String nickname;
    private String avatar;
    private Date birth;
    private int gender;  //1男 2女
    private String address;
    private String mobile;
    private String email;
    private String qq_openid;
    private String wx_openid;
    private Date createTime;
    private String code;
}
