package com.qc.printers.utils;

import com.qc.printers.common.MyString;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieManger {
    public static void setARCookie(HttpServletResponse response,String accessToken,String refreshToken){
        Cookie cookie = new Cookie(MyString.pre_cookie_access_token, accessToken);
        cookie.setPath("/");
        Cookie cookie2 = new Cookie(MyString.pre_cookie_refresh_token, refreshToken);
        cookie2.setPath("/");
        response.addCookie(cookie);
        response.addCookie(cookie2);
    }
    //清除cookies
    public static void cleanARCookie(HttpServletResponse response){
        Cookie cookie = new Cookie(MyString.pre_cookie_access_token, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        Cookie cookie2 = new Cookie(MyString.pre_cookie_refresh_token, null);
        cookie2.setMaxAge(0);
        cookie2.setPath("/");
        response.addCookie(cookie);
        response.addCookie(cookie2);
    }
}
