package com.resturant.management.utility;

import com.resturant.management.constants.Common;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
public class SessionChecker {

    public static String getCookies(HttpServletRequest request,String cookieName)
    {
        Cookie[] cookies = request.getCookies();
        Cookie loginCookie =null;
        String jwtToken=null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    loginCookie = cookie;
                    log.info("cookies name : {} cookies value : {}", loginCookie.getName(), loginCookie.getValue());
                    jwtToken= loginCookie.getValue();
                    break;
                }

            }
            return jwtToken;
        }
        else {
            return null;
        }
    }

    public static void deleteCookies(HttpServletRequest request, HttpServletResponse response,String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(cookieName)) {
                    Cookie loginCookie = cookie;
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    log.info("cookies name : {} cookies value : {}", loginCookie.getName(), loginCookie.getValue());
                    loginCookie.setMaxAge(0);
                    response.addCookie(loginCookie);
                    log.info("cookies name : {} cookies value : {}", loginCookie.getName(), loginCookie.getValue());
                    break;
                }
            }
        }

    }
}
