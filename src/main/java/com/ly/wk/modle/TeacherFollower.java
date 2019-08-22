package com.ly.wk.modle;

import lombok.Data;
import java.util.Date;

@Data
public class TeacherFollower {
    private Integer fid;
    private User user;
    private Teacher teacher;
    private Date create_time;
}
