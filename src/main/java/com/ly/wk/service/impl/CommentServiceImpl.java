package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.CommentMapper;
import com.ly.wk.modle.CourseComment;
import com.ly.wk.modle.User;
import com.ly.wk.service.CommentService;
import com.ly.wk.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired private CommentMapper commentMapper;
    @Autowired private DefaultParamBean defaultParamBean;
    @Autowired private HashOperations hashOperations;

    @Override
    public List<CourseComment> findByCid(int cid) {
        if(cid<=0)
            return null;
        List<CourseComment> comments=null;
        try{
            comments=commentMapper.getByCid(cid);
            Set<User> users=new HashSet<>();
            comments.stream().forEach(comment -> {
                users.add(comment.getUser());
                queryLikeCount(comment,cid);
            });
            users.stream().forEach(user -> {
                user.setAvatar(defaultParamBean.getImagePath()+user.getAvatar());
            });

        }catch (Throwable t){
            t.printStackTrace();
        }
        return comments;
    }

    @Override
    public int addComment(CommentVo commentVo) {
        if(commentVo.getContent()==null || "".equals(commentVo.getContent()))
            return 0;
        commentMapper.add(commentVo);
        return commentVo.getMid();
    }

    public void queryLikeCount(CourseComment comment,int cid){
        int mid=comment.getMid();
        Integer count= (Integer) hashOperations.get("comment_like_"+cid,mid+"");
        if(count==null)
            count=0;
        comment.setLike_count(count);
    }

    @Override
    public void like(int cid,int mid) {
        if(hashOperations.hasKey("comment_like_"+cid,mid+"")){
            hashOperations.increment("comment_like_"+cid,mid+"",1);
        }
        else{
            hashOperations.put("comment_like_"+cid,mid+"",1);
        }
    }

    @Override
    public void unlike(int cid,int mid) {
        if(hashOperations.hasKey("comment_like_"+cid,mid+"")){
            hashOperations.increment("comment_like_"+cid,mid+"",-1);
        }
        else{
            hashOperations.put("comment_like_"+cid,mid+"",1);
        }
    }
}
