package com.ly.wk.vo;

import java.util.HashMap;

public class ResultVo extends HashMap<String,Object> {
    private ResultVo(){}

    public  ResultVo put(String key,Object value){
        super.put(key,value);
        return this;
    }

    public static ResultVo of(Status status,String msg){
        ResultVo result=new ResultVo();
        result.put("status",status);
        return result.put("msg",msg);
    }

    public static ResultVo of(Status status){
        ResultVo result=new ResultVo();
        return result.put("status",status);
    }

    public static ResultVo of(Status status,String msg,Object data){
        ResultVo result=new ResultVo();
        result.put("status",status);
        result.put("data",data);
        return result.put("msg",msg);
    }
}
