package com.resturant.management.webcontroller;

import com.resturant.management.constants.Common;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.AdminService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class AdminWebController {

    @Value("${resourceUrl}")
    String baseUrl;

    @Autowired
    AdminService adminService;

    @RequestMapping("/admin/category")
    public ModelAndView addCategory(HttpServletRequest request)
    {
        ModelAndView md=new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtToken= SessionChecker.getCookies(request, Common.SESSION_ADMIN);
        if (jwtToken!=null) {
            md.addObject("userName",authentication.getName());
            md.setViewName("category");
        }
        else
        {
            md.setViewName("redirect:/admin/login");
        }

        return  md;
    }

    @RequestMapping("/admin/index")
    private ModelAndView adminIndex(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView md = new ModelAndView();
        String jwtToken= SessionChecker.getCookies(request, Common.SESSION_ADMIN);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(jwtToken!=null)
        {
            md.addObject("userName",authentication.getName());
            md.setViewName("admin-index");
        }
        else
        {
            md.setViewName("redirect:/admin/login");
        }
        return md;
    }

    @RequestMapping("/admin/menu")
    private ModelAndView addMenu(HttpServletRequest request)
    {
        ModelAndView md=new ModelAndView();
        String jwtToken= SessionChecker.getCookies(request, Common.SESSION_ADMIN);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (jwtToken!=null) {

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer"+jwtToken);
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            RestTemplate restTemplate=new RestTemplate();
            ResponseMessageDto template=  restTemplate.exchange(baseUrl+"/api/category/getCategories", HttpMethod.GET, entity, ResponseMessageDto.class).getBody();
            md.addObject("categories",template.getResponse());
            md.addObject("userName",authentication.getName());
            md.setViewName("menu");
        }
        else
        {
            md.setViewName("redirect:/admin/login");
        }

        return  md;
    }


    @RequestMapping("/admin/order")
    private ModelAndView addOrder(HttpServletRequest request)
    {
        //As the Order Module is not Implemented when it is implemented properly ad that time JWT token will be added the comment code will be uncomemnted
        ModelAndView md=new ModelAndView();
       /* String jwtToken= SessionChecker.getCookies(request, Common.SESSION_ADMIN);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(jwtToken!=null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer"+jwtToken);
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseMessageDto template = restTemplate.exchange(baseUrl + "/api/category/getCategories", HttpMethod.GET, entity, ResponseMessageDto.class).getBody();
            if (template.getIsErrorMessage()) {
                md.addObject("categories", template.getResponse());
            } else {
                throw new RuntimeException(template.getErrorMessage());
            }
            md.addObject("userName",authentication.getName());
            md.setViewName("order");
        }
        else
        {
            md.setViewName("redirect:/admin/login");
        }*/
        md.setViewName("order");

        return  md;
    }

    @RequestMapping("/admin/login")
    private ModelAndView adminLogin(HttpServletRequest request)
    {
        ModelAndView md=new ModelAndView();
        md.setViewName("admin/login");

        return  md;
    }

    @RequestMapping("/admin/logout")
    public ModelAndView adminLogout(HttpServletRequest request,HttpServletResponse response) {
        ModelAndView md=new ModelAndView();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        adminService.logout(authentication.getName());
        SessionChecker.deleteCookies(request,response,Common.SESSION_ADMIN);
        md.setViewName("redirect:/admin/login");
        return   md;
    }
}
