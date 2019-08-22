package com.ly.wk.utils;

import com.ly.wk.modle.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static final int MAX_AGE=24*60*60;   //一天

    public static void addCookie(HttpServletResponse resp, User user){
        Cookie username=new Cookie("username",user.getUsername());
        Cookie password=new Cookie("password",user.getPassword());

        username.setMaxAge(MAX_AGE);
        password.setMaxAge(MAX_AGE);

        resp.addCookie(username);
        resp.addCookie(password);
    }

    public static void removeCookie(HttpServletRequest req){
        Cookie[] cookies=req.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("username") || cookie.getName().equals("password"))
                cookie.setMaxAge(0);
        }
    }
}
