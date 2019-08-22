package com.ly.wk.service;

import com.ly.wk.modle.User;
import com.ly.wk.vo.PersonalDataVo;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResultVo register(UserVo userVo);
    User login(User user);
    boolean sendCode(String mobile);
    boolean verifyUsername(String username);
    boolean verifyEmail(String email);
    boolean verifyMobile(String mobile);
    ResultVo updatePersonalData(MultipartFile file, PersonalDataVo personalDataVo);
    PersonalDataVo showData(String uid);
}
