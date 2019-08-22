package com.ly.wk.vo;

import lombok.Data;

import java.util.Date;

@Data
public class PersonalDataVo {
    private String uid;
    private String nickname;
    private String avatar;
    private String birthday;
    private Date birth;
    private int gender;
    private String address;
    private boolean isMan;
}
