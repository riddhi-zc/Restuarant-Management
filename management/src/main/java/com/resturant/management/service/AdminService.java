package com.resturant.management.service;

import com.resturant.management.dto.LoginDto;
import com.resturant.management.entity.UserModel;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface AdminService {

    UserModel getUserData(Principal principal);

    Object login(HttpServletResponse response, LoginDto dto);

    void logout(String name);
}
