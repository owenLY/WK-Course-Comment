package com.ly.wk.controller;

import com.ly.wk.modle.*;
import com.ly.wk.service.CommentService;
import com.ly.wk.service.CourseService;
import com.ly.wk.service.NoteService;
import com.ly.wk.vo.CommentVo;
import com.ly.wk.vo.Condition;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@RequestMapping("/course")
@RestController
public class CourseController {
    private Logger logger= LoggerFactory.getLogger(CourseController.class);
    @Autowired CourseService courseService;
    @Autowired CommentService commentService;
    @Autowired NoteService noteService;
    @Autowired HttpSession session;

    @RequestMapping("/details/{cid}")
    public ModelAndView details(@PathVariable("cid") String cid, ModelAndView mv){
        int id=0;
        try{
            id=Integer.parseInt(cid);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        User user= (User) session.getAttribute("user");
        int buy_state= (user==null)? 0 : courseService.queryUserCourse(user.getUid(),id);

        Course course=courseService.queryCourseByCid(id);

        if(course!=null) {
            mv.setViewName("details");
            mv.addObject("course",course);
            mv.addObject("buy_state",buy_state);
        }
        else{
            mv.setViewName("redirect:error/error.html");
        }
        return mv;
    }

    @RequestMapping("/video/{cid}/{vid}")
    public ModelAndView video(@PathVariable("cid") String cid ,@PathVariable("vid") String vid,ModelAndView mv){
        int id=0;
        int c_id=0;
        try{
            id=Integer.parseInt(vid);
            c_id=Integer.parseInt(cid);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        User user= (User) session.getAttribute("user");
        int buy_state= (user==null)? 0 : courseService.queryUserCourse(user.getUid(),id);

        Video video=courseService.queryVideoByVid(id);
        Course course=courseService.queryCourseByCid(c_id);
        if(user!=null)
        {
            Long currentTime=courseService.findCurrentTime(user.getUid(),video.getVid());
            if(currentTime!=null) {
                mv.addObject("currentTime", currentTime.longValue());
            }
        }
        if(video!=null && course!=null){
            mv.addObject("video",video);
            mv.addObject("course",course);
            mv.addObject("buy_state",buy_state);
            mv.setViewName("video");
        }
        else{
            mv.setViewName("redirect:error/error.html");
        }
        return mv;
    }

    @RequestMapping("/showCourse")
    public ModelAndView showCourse(ModelAndView mv){
        List<CourseType> parentTypes=courseService.queryAllCourseType();
        if(parentTypes!=null){
            mv.setViewName("course");
            mv.addObject("parentTypes",parentTypes);
        }
        else{
            mv.setViewName("redirect:error/error.html");
        }
        return mv;
    }

    @RequestMapping("/queryCourse")
    public ResultVo queryCourse(@RequestBody Condition condition){
        logger.info(condition.toString());
        List<Course> courses=courseService.queryCourseByCondition(condition);
        int total=courseService.queryPageCount(condition);
        ResultVo resultVo;
        if(courses!=null) {
            resultVo = ResultVo.of(Status.SUCCESS, "找到" +total + "门课程", courses);
            resultVo.put("total",total);
        } else
            resultVo=ResultVo.of(Status.FAILURE,"暂时找不到课程");
        return resultVo;
    }

    @RequestMapping("/showComment/{cid}")
    public ResultVo showComment(@PathVariable String cid){
        int c_id=0;
        try {
            c_id=Integer.parseInt(cid);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        List<CourseComment> comments=commentService.findByCid(c_id);
        if(comments!=null){
            logger.info(comments.toString());
            return  ResultVo.of(Status.SUCCESS,"找到"+comments.size()+"条评论",comments);
        }
        return ResultVo.of(Status.FAILURE,"未找到评论");
    }

    @RequestMapping("/submitComment")
    public ResultVo submitComment(@RequestBody CommentVo commentVo){
        logger.info(commentVo.toString());
        int mid=commentService.addComment(commentVo);
        if(mid!=0){
            return ResultVo.of(Status.SUCCESS,"评价成功!",mid);
        }
        else {
            return ResultVo.of(Status.FAILURE,"评价失败");
        }
    }

    @RequestMapping("/comment/like/{cid}/{mid}")
    public void like(@PathVariable String cid,@PathVariable String mid){
        int m_id;
        int c_id;
        try{
            c_id= Integer.parseInt(cid);
            m_id=Integer.parseInt(mid);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return;
        }
        commentService.like(c_id,m_id);
    }
    @RequestMapping("/comment/unlike/{cid}/{mid}")
    public void unlike(@PathVariable String cid,@PathVariable String mid){
        int  m_id=Integer.parseInt(mid);
        int  c_id= Integer.parseInt(cid);
        commentService.unlike(c_id,m_id);
    }


    @RequestMapping("/showNote/{cid}")
    public ResultVo showNote(@PathVariable String cid){
        int c_id=0;
        try{
            c_id=Integer.parseInt(cid);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        List<Note> notes=noteService.findNoteByCid(c_id);

        if(notes!=null)
            return ResultVo.of(Status.SUCCESS,"共"+notes.size()+"个笔记",notes);
        else
            return ResultVo.of(Status.FAILURE,"未查到数据");
    }

    @RequestMapping("/showMyCourse")
    public ResultVo showMyCourse(){
        User user= (User) session.getAttribute("user");
        String uid=user.getUid();
        List<UserCourse> list=courseService.showMyCourse(uid);
        return ResultVo.of(Status.SUCCESS,"",list);
    }

    @RequestMapping(value="/search",method=RequestMethod.GET)
    public ModelAndView search(@RequestParam String q, ModelAndView mv){
        List<Course> courses=courseService.search(q);
        mv.addObject("courses",courses);
        mv.setViewName("search-result");
        return mv;
    }


    @RequestMapping("/schedule")
    public void saveSchedule(@RequestParam String vid,@RequestParam String curr){
        System.err.println(curr);
        long currentTime=Long.parseLong(curr);
        int _vid= Integer.parseInt(vid);
        User user= (User) session.getAttribute("user");
        System.err.println(user);
        String uid=user.getUid();
        courseService.saveVideoSchedule(uid,_vid,currentTime);
    }

    @RequestMapping("/study/{cid}")
    public ModelAndView joinStudy(@PathVariable String cid,ModelAndView mv){
        Integer _cid=Integer.parseInt(cid);
        String uid=null;
        User user= (User) session.getAttribute("user");
        if(user!=null)
            uid=user.getUid();

        courseService.joinStudy(mv,uid,_cid);
        mv.setViewName("video");
        return mv;
    }
}
