package com.ly.wk.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentVo implements Serializable {
    public static final long serialVersionUID=1L;
    private Integer mid;
    private String uid;
    private Integer cid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date add_time;
    private String content;
}
