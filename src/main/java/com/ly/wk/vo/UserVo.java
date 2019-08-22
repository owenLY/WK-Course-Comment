package com.ly.wk.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    private String uid;
    private String username;
    private String email;
    private String mobile;
    private String code;
    private String password;
    private String salt;
    private int state=1;
    private Date createTime;
    private String avatar;
    private String nickname;
    private int gender=1;
}
