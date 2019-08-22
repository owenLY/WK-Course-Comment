package com.ly.wk.modle;

import lombok.Data;

import java.util.Date;

@Data
public class Schedule {
    private Integer sid;
    private Long currentTime;
    private Date createTime;
    private String uid;
    private Video video;
}
