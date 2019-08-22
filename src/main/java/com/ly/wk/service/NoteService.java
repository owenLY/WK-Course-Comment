package com.ly.wk.service;

import com.ly.wk.modle.Note;

import java.util.List;

public interface NoteService {
    List<Note> findNoteByCid(int cid);
}
