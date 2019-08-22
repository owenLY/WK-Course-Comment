package com.ly.wk.service;

import com.ly.wk.vo.ResultVo;

public interface FollowService {
    ResultVo updateFollow(String uid, int tid, String follow);
    int findFollow(String uid,int tid);
}
