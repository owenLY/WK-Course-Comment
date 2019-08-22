package com.ly.wk.service.impl;

import com.ly.wk.config.AlipayConfig;
import com.ly.wk.config.DefaultParamBean;
import com.ly.wk.event.OrderTradeSuccessEvent;
import com.ly.wk.mapper.*;
import com.ly.wk.modle.Course;
import com.ly.wk.modle.Order;
import com.ly.wk.modle.OrderItem;
import com.ly.wk.service.AlipayService;
import com.ly.wk.service.CarService;
import com.ly.wk.service.OrderService;
import com.ly.wk.utils.SpringUtils;
import com.ly.wk.utils.UUidUtil;
import com.ly.wk.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 支付宝服务
 */
@Service
public class OrderServiceImpl implements OrderService {
    private Logger logger= LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CarService carService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper itemMapper;
    @Autowired
    DefaultParamBean defaultParamBean;
    @Autowired
    AlipayService alipayService;
    @Autowired
    UserCourseMapper userCourseMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AlipayConfig alipayConfig;


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Order submitOrder(String uid,Integer[] cids){
        int len=cids.length;
        StringBuffer params=new StringBuffer();
        for(int i=0;i<len;i++){
            params.append(cids[i]);
            if(i!=len-1)
                params.append(",");
        }

        Order order=null;

        try {
            String[] arr=params.toString().split(",");

            String oid= UUidUtil.getNowUUid()+new Random().nextInt(9999);
            double total_money=courseMapper.getPriceSum(params.toString());
            Date create_time=new Date(System.currentTimeMillis());
            OrderVo orderVo=new OrderVo(oid,total_money,create_time,-1,uid);

            int m=orderMapper.addOrder(orderVo);
            if(m!=1)
                throw new Throwable();

            int t=0;
            for(int i=0;i<len;i++){
                t=t+itemMapper.addItem(cids[i],oid);
            }

            if(t!=len)
                throw new Throwable();

            carService.removeItem(uid,arr);

            order=orderMapper.getByOid(oid);
            order.setOid(oid);
            order.getItems().stream().forEach(orderItem -> {
                Course course=orderItem.getCourse();
                course.setImage(defaultParamBean.getImagePath()+course.getImage());
            });

            logger.info(order.toString());

        }catch (Throwable t){
            logger.info("-------------rollback-----------------");
            t.printStackTrace();
        }

        return order;
    }


    @Override
    public String prePay(String oid) {
        Order order=orderMapper.getByOid(oid);
        double total_amount=order.getTotal_money();
        String subject="微课网课程支付";
        String body=alipayService.alipay(oid,total_amount,subject);
        return body;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String dealOrderResult(Map<String, String> params) {
        //根据返回的订单来验证信息

        //商户app_id
        String app_id = params.get("app_id");
        if (app_id == null || !app_id.equals(alipayConfig.app_id))
            return "fail";

        //订单号
        String oid = params.get("out_trade_no");
        Order order = orderMapper.getByOid(oid);
        if (order == null || order.getTotal_money() == null)
            return "fail";

        //订单金额
        Double total_amount= Double.parseDouble(params.get("total_amount"));
        Double total_money=order.getTotal_money();
        if (total_amount == null || total_amount.doubleValue()!=total_money.doubleValue())
            return "fail";

        //验证签名
        boolean flag =alipayService.signVerified(params,alipayConfig.alipay_public_key,alipayConfig.charset,alipayConfig.sign_type);
        logger.info("签名验证结果：\t"+flag);

        if(flag){
            //以上条件满足则修改订单等数据

            //支付宝交易号
            String trade_no=params.get("trade_no");
            //交易状态
            String trade_status=params.get("trade_status");
            logger.info("交易状态:\t"+trade_status);

            if(trade_status.equals("TRADE_FINISHED")){  //交易完成


            }
            else if(trade_status.equals("TRADE_SUCCESS")){  //交易支付成功

                try{
                    //修改订单状态及支付宝交易号
                    int n=orderMapper.updateStateAndAlipay(oid,1,trade_no);
                    if(n!=1)
                        throw new Throwable();

                    //给用户添加课程
                    String uid=order.getUser().getUid();
                    Date now=new Date(System.currentTimeMillis());
                    int m=0;
                    for(Iterator<OrderItem> it=order.getItems().iterator();it.hasNext();){
                        OrderItem item=it.next();
                        int cid=item.getCourse().getCid();
                        m+=userCourseMapper.addUC(now,uid,cid);
                    }

                    if(m!=order.getItems().size())
                        throw new Throwable();
                }catch (Throwable t){
                    //发生异常
                    //回滚
                    //退款
                    t.printStackTrace();
                    return "fail";
                }

            }

           return "success";
        }

        return "fail";
    }

    @Override
    public boolean verifyOrderResult(Map<String, String> params) {
        //商户app_id
        String app_id = params.get("app_id");
        if (app_id == null || !app_id.equals(alipayConfig.app_id))
            return false;
        //验证签名
        boolean flag =alipayService.signVerified(params,alipayConfig.alipay_public_key,alipayConfig.charset,alipayConfig.sign_type);

        if(flag){
            String oid = params.get("out_trade_no");
            String uid=orderMapper.getUid(oid);
            String mobile=userMapper.getMobile(uid);

            double total_amount=Double.parseDouble(params.get("total_amount"));
            String text="【云片网】您购买的"+oid+"订单已成功消费，祝您生活愉快！";

            int state=orderMapper.findState(oid);   //验证订单状态是否为１,既是否已经付款
            if(state==1) {  //确认订单交易成功，发布订单交易成功事件
                SpringUtils.getApplicationContext()
                        .publishEvent(new OrderTradeSuccessEvent(this,mobile,text));
                logger.info("事件发布成功");
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Order> queryOrder(String uid, int state) {
        List<Order>  orders=orderMapper.getByUid(uid,state);
        Set<Course> courses=new HashSet<>();
        orders.stream().forEach(order -> {
            Set<OrderItem> items=order.getItems();
            for(Iterator<OrderItem> it=items.iterator();it.hasNext();){
                OrderItem item=it.next();
                Course course=item.getCourse();
                courses.add(course);
            }
        });
        courses.stream().forEach(course -> course.setImage(defaultParamBean.getImagePath()+course.getImage()));
        return orders;
    }


}
