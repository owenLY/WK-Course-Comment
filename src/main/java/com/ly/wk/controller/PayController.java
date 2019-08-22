package com.ly.wk.controller;

import com.ly.wk.modle.Course;
import com.ly.wk.modle.Order;
import com.ly.wk.modle.User;
import com.ly.wk.service.CarService;
import com.ly.wk.service.OrderService;
import com.ly.wk.vo.ResultVo;
import com.ly.wk.vo.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Component
@RequestMapping("/pay")
@RestController
public class PayController {
    private Logger logger= LoggerFactory.getLogger(PayController.class);
    @Autowired
    private HttpSession session;
    @Autowired
    private CarService carService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/car")
    public ModelAndView showCar(ModelAndView mv){
        User user= (User) session.getAttribute("user");
        if(user==null){
            mv.setViewName("redirect:login.html");
            return mv;
        }

        String  uid=user.getUid();
        List<Course> car=carService.showCar(uid);
        logger.info("购物车:\t"+car);
        if(car!=null) {
            mv.addObject("car", car);
            mv.setViewName("car");
        }
        return mv;
    }

    @RequestMapping("/add2car/{cid}")
    public ResultVo add2car(@PathVariable String cid){
        User user= (User) session.getAttribute("user");
        if(user==null)
            return ResultVo.of(Status.FAILURE,"请先登录");
        String uid=user.getUid();

        String result=carService.addCourse2Car(uid,cid);
        if(!"添加失败".equals(result))
            return ResultVo.of(Status.SUCCESS,"添加成功");
        else
            return ResultVo.of(Status.FAILURE,"添加失败");
    }


    @RequestMapping("/remove/{cid}")
    public ResultVo removeItem(@PathVariable String cid){
        User user= (User) session.getAttribute("user");
        String uid=user.getUid();

        long n=carService.removeItem(uid,cid);
        if(n==1)
            return ResultVo.of(Status.SUCCESS);
        return ResultVo.of(Status.FAILURE);
    }

    @RequestMapping("/submit")
    public ModelAndView submitOrder(@RequestParam String cids,ModelAndView mv){
        String[] arr=cids.split(",");
        Integer[] _cids=new Integer[arr.length];

        for(int i=0;i<arr.length;i++){
            int n=Integer.parseInt(arr[i]);
            _cids[i]=n;
        }

        logger.info(Arrays.asList(_cids).toString());
        User user= (User) session.getAttribute("user");
        String uid=user.getUid();

        Order order=orderService.submitOrder(uid,_cids);
        if(order!=null) {
            mv.addObject("order", order);
            mv.setViewName("order");
        }
        else {
            mv.setViewName("redirect:error/error.html");
        }
        return mv;
    }

    @RequestMapping("/2alipay")
    public void pay(@RequestParam String oid, HttpServletResponse response){
        String body=orderService.prePay(oid);
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print("<html><body>"+body+"</body></html>");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 注意；在支付宝的回调过程中无法使用session cookie
    @RequestMapping(value = "/deal_notify",method = RequestMethod.POST)
    public String  dealNotify(HttpServletRequest request){
        System.out.println("notify到达");
        Map<String,String> params=parseRequest(request);
        logger.info(params.toString()) ;

        return orderService.dealOrderResult(params);
    }

    @RequestMapping("/deal_return")
    public ModelAndView dealReturn(ModelAndView mv,HttpServletRequest request){
        System.out.println("return到达");
        Map<String,String> params=parseRequest(request);
        logger.info(params.toString());

        boolean flag=orderService.verifyOrderResult(params);
        if(flag){
            mv.setViewName("pay_success");
        }
        else{
            mv.setViewName("error/error");
        }

        return mv;
    }

    //获取支付宝POST过来notified信息
    public Map<String,String> parseRequest(HttpServletRequest request){

        Map<String,String[]> requestParams=request.getParameterMap();
        Map<String,String> params=new HashMap<>();

        for(Iterator<String> it=requestParams.keySet().iterator();it.hasNext();){
            String name=it.next();
            String[] values=requestParams.get(name);
            String valueStr="";
            for(int i=0;i<values.length;i++){
                valueStr=(i==values.length-1) ? valueStr+values[i] : valueStr+values[i]+",";
            }
            params.put(name,valueStr);
        }

        return params;
    }
}
