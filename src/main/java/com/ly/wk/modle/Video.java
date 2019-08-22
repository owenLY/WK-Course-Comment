package com.ly.wk.modle;

import lombok.Data;

@Data
public class Video {
    private Integer vid;
    private String name;
    private String url;
    private String type;
    private Integer sort;

    private Lesson lesson;
}
