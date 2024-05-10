package com.resturant.management.service;

import com.resturant.management.dto.LoginDto;
import com.resturant.management.dto.RegisterDto;
import com.resturant.management.entity.UserModel;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface UserService {
    UserModel getUserData(Principal principal);

    Object login(HttpServletResponse response, LoginDto dto);

    void register(RegisterDto dto);

    void logout(String userName);

}
