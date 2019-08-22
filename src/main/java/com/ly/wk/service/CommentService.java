package com.ly.wk.service;

import com.ly.wk.modle.CourseComment;
import com.ly.wk.vo.CommentVo;

import java.util.List;

public interface CommentService {

    List<CourseComment> findByCid(int cid);
    int addComment(CommentVo commentVo);
    void like(int cid,int mid);
    void unlike(int cid,int mid);
}
