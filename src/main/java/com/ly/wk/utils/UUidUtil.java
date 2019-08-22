package com.ly.wk.utils;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

//获取UUID的工具
public class UUidUtil {
    private static Random random=new Random();

    public static String getUUid(){
        String str= UUID.randomUUID().toString();
        return str.replaceAll("-","");
    }

    public static String getNowUUid(){
        LocalDateTime localDateTime=LocalDateTime.now();
        int year=localDateTime.getYear();
        int month=localDateTime.getMonthValue();
        int day=localDateTime.getDayOfMonth();
        int hour=localDateTime.getHour();
        int minute=localDateTime.getMinute();
        int second=localDateTime.getSecond();
        return ""+year+month+day+hour+minute+second;
    }

    public static String get4Number(){
        String[] numbers={"1","2","3","4","5","6","7","8","9"};
        StringBuffer str=new StringBuffer();
        for(int i=0;i<4;i++){
            str.append(numbers[random.nextInt(9)]);
        }
        return str.toString();
    }

    public static String get6Character(){
        String[] _char={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        StringBuffer str=new StringBuffer();
        for(int i=0;i<8;i++){
            str.append(_char[random.nextInt(26)]);
        }
        return str.toString();
    }
}
