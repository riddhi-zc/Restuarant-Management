package com.resturant.management.webcontroller;

import com.resturant.management.constants.Common;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.UserService;
import com.resturant.management.utility.SessionChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class UserWebController {

    @Value("${resourceUrl}")
    String baseUrl;

    @Autowired
    UserService userService;

    @RequestMapping("/user/menu")
    public ModelAndView getMenu(HttpServletRequest request)
    {
        ModelAndView md=new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtToken= SessionChecker.getCookies(request, Common.SESSION_USER);
        if (jwtToken!=null) {
            md.addObject("userName",authentication.getName());
            md.setViewName("menuScreen");
        }
        else
        {
            md.setViewName("redirect:/user/login");
        }
        return  md;
    }


    @RequestMapping("/user/index")
    private ModelAndView userIndex(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView md = new ModelAndView();
        log.info("{}",request.getCookies());
        String jwtToken= SessionChecker.getCookies(request, Common.SESSION_USER);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (jwtToken!=null) {
            md.addObject("userName",authentication.getName());
            md.setViewName("index");
        }
        else
        {
            md.setViewName("redirect:/user/login");
        }
        return md;
    }

    @RequestMapping("/user/login")
    private ModelAndView userLogin(HttpServletRequest request)
    {
        ModelAndView md=new ModelAndView();
        md.setViewName("user/login");
        return  md;
    }


    @RequestMapping("/user/register")
    private ModelAndView userRegister(HttpServletRequest request)
    {
        ModelAndView md=new ModelAndView();
        md.setViewName("user/register");
        return  md;
    }

    @RequestMapping("/user/logout")
    public ModelAndView userLogout(HttpServletRequest request,HttpServletResponse response) {
        ModelAndView md=new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.logout(authentication.getName());
        SessionChecker.deleteCookies(request,response,Common.SESSION_USER);
        md.setViewName("redirect:/user/login");
        return   md;
    }

}
