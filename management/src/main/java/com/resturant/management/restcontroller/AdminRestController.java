package com.resturant.management.restcontroller;


import com.resturant.management.constants.Common;
import com.resturant.management.dto.LoginDto;
import com.resturant.management.dto.RegisterDto;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.AdminService;
import com.resturant.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/auth/admin")
public class AdminRestController {

    @Autowired
    private AdminService amdminService;


    @PostMapping("/login")
    public ResponseEntity<ResponseMessageDto> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto dto) {
        try {
            return ResponseEntity.ok(new ResponseMessageDto(Boolean.TRUE, null, this.amdminService.login(response, dto)));
        } finally {

        }


    }

}
