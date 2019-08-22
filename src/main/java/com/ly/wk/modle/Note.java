package com.ly.wk.modle;

import lombok.Data;
import java.util.Date;

@Data
public class Note {
    private Integer nid;
    private String content;
    private Date create_time;
    private Course course;
    private User user;
}
