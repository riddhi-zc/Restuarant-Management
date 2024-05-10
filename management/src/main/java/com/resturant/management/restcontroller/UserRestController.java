package com.resturant.management.restcontroller;

import com.resturant.management.constants.Common;
import com.resturant.management.dto.LoginDto;
import com.resturant.management.dto.RegisterDto;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessageDto> registerUser(HttpServletRequest request, @RequestBody RegisterDto dto) {
        try {
            this.userService.register(dto);
            return ResponseEntity.ok(new ResponseMessageDto(Boolean.TRUE, null, Common.REGISTERED_SUCCESSFULLY));
        } finally {

        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessageDto> login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDto dto) {
        try {
            return ResponseEntity.ok(new ResponseMessageDto(Boolean.TRUE, null, this.userService.login(response, dto)));
        } finally {

        }
    }
}
