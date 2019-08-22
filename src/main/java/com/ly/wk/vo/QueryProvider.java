package com.ly.wk.vo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Iterator;
import java.util.Map;

public class QueryProvider {

    public String queryByCondition(@Param("condition") Map<String,Object> condition){
        SQL sql=new SQL();
        sql.SELECT("cid","name","degree","price","favorite_num","image")
                .FROM("course");
        Iterator<Map.Entry<String,Object>> it=condition.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Object> en = it.next();
            if ("degree".equals(en.getKey())) {
                sql.WHERE("degree=#{degree}");
            }
            if ("price".equals(en.getKey())) {
                sql.WHERE("price=#{price}");
            }
            if ("sort".equals(en.getKey())) {
                sql.ORDER_BY("${sort} desc");
            }
        }
        sql.LIMIT(5);
        return sql.toString();
    }

    public String queryPage(@Param("condition") Condition condition){
        SQL sql=new SQL()
                .SELECT("cid","name","degree","price","image")
                .FROM("course");
        if(condition.getTid()!=null && condition.getTid()!=0)
            sql.WHERE("tid=#{tid}");
        if(condition.getPrice()!=null)
            sql.WHERE("price=#{price}");
        if(condition.getDegree()!=null && condition.getDegree()!=0)
            sql.WHERE("degree=#{degree}");
        if(condition.getSort()!=null)
            sql.ORDER_BY("${sort} desc");
        if(condition.getStart()!=null && condition.getSize()!=null)
            sql.LIMIT("#{start},#{size}");
        return  sql.toString();
    }

    public String queryPageCount(@Param("condition") Condition condition){
        SQL sql=new SQL()
                .SELECT("count(*)")
                .FROM("course");
        if(condition.getTid()!=null && condition.getTid()!=0)
            sql.WHERE("tid=#{tid}");
        if(condition.getDegree()!=null && condition.getDegree()!=0)
            sql.WHERE("degree=#{degree}");
        if(condition.getSort()!=null)
            sql.ORDER_BY("${sort} desc");
        return sql.toString();
    }

    public String queryOrder(String uid,int state){
        SQL sql=new SQL()
                .SELECT("oid,total_money,create_time,state")
                .FROM("_order");
        sql.WHERE(" uid=#{uid}");
        if(state!=0)
            sql.WHERE("state=#{state}");
        sql.ORDER_BY("create_time desc");
        return sql.toString();
    }

    public String updateUser(PersonalDataVo personalData){
        SQL sql=new SQL()
                .UPDATE("user");
        if(personalData.getNickname()!=null)
            sql.SET("nickname=#{nickname}");
        if(personalData.getAddress()!=null)
            sql.SET("address=#{address}");
        if(personalData.getBirth()!=null)
            sql.SET("birth=#{birth}");
        if(personalData.getAvatar()!=null)
            sql.SET("avatar=#{avatar}");
        if(personalData.getGender()!=0)
            sql.SET("gender=#{gender}");

        sql.WHERE("uid=#{uid}");
        return sql.toString();
    }
}
