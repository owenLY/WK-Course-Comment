package com.ly.wk.controller;

import com.ly.wk.modle.User;
import com.ly.wk.service.FollowService;
import com.ly.wk.service.UserService;
import com.ly.wk.vo.PersonalDataVo;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.Status;
import com.ly.wk.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserController {
    private Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired private UserService userService;
    @Autowired private HttpSession session;
    @Autowired private FollowService followService;


    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ResultVo login(@RequestBody User user) {
        ResultVo resultVo=ResultVo.of(Status.FAILURE);
        if(user!=null){
            User u=userService.login(user);
            if(u!=null){
                session.setAttribute("user",u);
                resultVo=ResultVo.of(Status.SUCCESS,"登录成功!");
                logger.info(u.toString());
            }
        }
        return resultVo;
    }

    @RequestMapping("/register")
    public ResultVo register(@RequestBody UserVo userVo){
        logger.info(userVo.toString());
       return userService.register(userVo);
    }

    @RequestMapping("/follow/{tid}/{follow}")
    public ResultVo followTeacher(@PathVariable String tid,@PathVariable String follow){
        User user= (User) session.getAttribute("user");
        if(user==null){
            return ResultVo.of(Status.FAILURE,"请先登录!");
        }
        String uid=user.getUid();
        int t_id=Integer.parseInt(tid);

        return followService.updateFollow(uid,t_id,follow);
    }

    @RequestMapping("/logout")
    public ModelAndView logout(ModelAndView mv){
        session.removeAttribute("user");
        mv.setViewName("redirect:/index.html");
        return mv;
    }

    @RequestMapping("/sendCode")
    public ResultVo sendCode(@RequestParam String mobile){
        boolean flag=userService.sendCode(mobile);
        if(flag)
            return ResultVo.of(Status.SUCCESS,"验证码发送成功");
        else
            return ResultVo.of(Status.FAILURE,"验证码发送失败");
    }

    @RequestMapping("/verifyUsername")
    public ResultVo verifyUsername(@RequestParam String username){
        boolean flag=userService.verifyUsername(username);
        if(flag)
            return ResultVo.of(Status.SUCCESS);
        else
            return ResultVo.of(Status.FAILURE);
    }

    @RequestMapping("/verifyEmail")
    public ResultVo verifyEmail(@RequestParam String email){
        boolean flag=userService.verifyEmail(email);
        if(flag)
            return ResultVo.of(Status.SUCCESS);
        else
            return ResultVo.of(Status.FAILURE);
    }

    @RequestMapping("/verifyMobile")
    public ResultVo verifyMobile(@RequestParam String mobile){
        boolean flag=userService.verifyMobile(mobile);
        if(flag)
            return ResultVo.of(Status.SUCCESS);
        else
            return ResultVo.of(Status.FAILURE);
    }

    @RequestMapping("/update")
    public ResultVo updatePersonalData(@RequestParam("file") MultipartFile file,  PersonalDataVo personalData){
       User user=(User)session.getAttribute("user");
       String uid=user.getUid();
       personalData.setUid(uid);
       ResultVo resultVo=userService.updatePersonalData(file,personalData);
       String avatar= (String) resultVo.get("data");
       if(avatar!=null)
           user.setAvatar(avatar);
       return resultVo;
    }
}
