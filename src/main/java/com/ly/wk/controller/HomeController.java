package com.ly.wk.controller;

import com.ly.wk.modle.Banner;
import com.ly.wk.modle.Order;
import com.ly.wk.modle.Teacher;
import com.ly.wk.modle.User;
import com.ly.wk.service.*;
import com.ly.wk.vo.CourseListVo;
import com.ly.wk.vo.PersonalDataVo;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HomeController {
    private Logger logger= LoggerFactory.getLogger(HomeController.class);
    @Autowired private BannerSerice bannerSerice;
    @Autowired private CourseService courseService;
    @Autowired private TeacherService teacherService;
    @Autowired private FollowService followService;
    @Autowired private OrderService orderService;
    @Autowired private UserService userService;

    @RequestMapping(value={"/","/index.html"})
    public ModelAndView index(ModelAndView mv){
        List<Banner> banners=bannerSerice.queryBanner();
        CourseListVo courseListVo=courseService.queryCourseList();
        if(banners!=null){
            mv.addObject("banners",banners);
            mv.addObject("courseListVo",courseListVo);
            mv.setViewName("index");
            return mv;
        }
        mv.setViewName("redirect:/error/error.html");
        return mv;
    }

    @RequestMapping("/home/ta/{tid}")
    public String taView(@PathVariable String tid, Model model,HttpSession session){
        int t_id=0;
        try{
            t_id=Integer.parseInt(tid);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        Teacher teacher=teacherService.findByTid(t_id);

        User user= (User) session.getAttribute("user");
        if(user!=null) {
            String uid = user.getUid();
            int follow = followService.findFollow(uid, t_id);
            System.err.println(follow);
            model.addAttribute("follow",follow+"");
        }
        else{
            model.addAttribute("follow",'0');
        }
        if(teacher!=null) {
            model.addAttribute("teacher", teacher);
        }
        else{
            return "redirect:/error/error.html";
        }
        return "ta";
    }

    @RequestMapping("/home/wo")
    public String woView(){
        return "wo";
    }


    @ResponseBody
    @RequestMapping("/queryOrder/{state}")
    public ResultVo queryOrder(@PathVariable String state,HttpSession session){
        User user= (User) session.getAttribute("user");
        String uid=user.getUid();

        int _state=Integer.parseInt(state);
        List<Order> orders=orderService.queryOrder(uid,_state);
        logger.info(orders.toString());

        return ResultVo.of(Status.SUCCESS,"",orders);
    }

    @RequestMapping("/home/showOrder")
    public String showOrder(){
        return "my-order";
    }

    @RequestMapping("/home/data")
    public ModelAndView toPersonalData(HttpSession session,ModelAndView mv){
        String uid=((User)session.getAttribute("user")).getUid();
        PersonalDataVo data=userService.showData(uid);
        mv.setViewName("personal-data");
        mv.addObject("data",data);
        return mv;
    }
}
