package com.ly.wk.service.impl;

import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.mapper.NoteMapper;
import com.ly.wk.modle.Note;
import com.ly.wk.modle.User;
import com.ly.wk.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired private NoteMapper noteMapper;
    @Autowired private DefaultParamBean defaultParamBean;

    @Override
    public List<Note> findNoteByCid(int cid) {
        if(cid==0)
            return null;
        List<Note> notes = noteMapper.getByCid(cid);
        Set<User> users=new HashSet<>();
        for(Note note:notes)
            users.add(note.getUser());
        users.stream().forEach(user->{
            user.setAvatar(defaultParamBean.getImagePath()+user.getAvatar());
        });
        return notes;
    }
}
