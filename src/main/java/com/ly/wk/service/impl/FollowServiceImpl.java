package com.ly.wk.service.impl;

import com.ly.wk.mapper.FollowMapper;
import com.ly.wk.mapper.TeacherMapper;
import com.ly.wk.service.FollowService;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public ResultVo updateFollow(String uid, int tid, String follow) {
        try {
            if ("0".equals(follow)) {
                int n = followMapper.add(uid, tid);
                //修改teacher表的粉丝数
                int m = teacherMapper.addFans(tid);

                if (n == 1 && m == 1)
                    return ResultVo.of(Status.SUCCESS, "操作成功!", "取消关注");
            } else if ("1".equals(follow)) {
                int n = followMapper.remove(uid, tid);
                //修改teacher的粉丝数
                int m = teacherMapper.removeFans(tid);

                if (n == 1 && m == 1)
                    return ResultVo.of(Status.SUCCESS, "操作成功!", "关注");
            }
        }catch (Throwable t){
            throw t;
        }
        return ResultVo.of(Status.FAILURE,"操作失败!");
    }

    @Override
    public int findFollow(String uid, int tid) {
        return followMapper.find(uid,tid);
    }
}
