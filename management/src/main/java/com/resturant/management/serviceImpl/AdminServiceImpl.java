package com.resturant.management.serviceImpl;

import com.resturant.management.constants.Common;
import com.resturant.management.dto.LoginDto;
import com.resturant.management.entity.Role;
import com.resturant.management.entity.UserModel;
import com.resturant.management.entity.UserTokenModel;
import com.resturant.management.repository.UserRepository;
import com.resturant.management.repository.UserTokenRespository;
import com.resturant.management.response.AuthenticationResponse;
import com.resturant.management.service.AdminService;
import com.resturant.management.utility.jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserTokenRespository tokenRespository;


    @Override
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_UNCOMMITTED)
    public UserModel getUserData(Principal principal) {
        log.info("getUserData");
        Optional<UserModel> user = null;
        if (principal != null && principal.getName() != null) {
            user = this.userRepository.findByUserName(principal.getName());
        }
        return user.get();
    }

    @Override
    public Object login(HttpServletResponse response, LoginDto dto) {

        Optional<UserModel> userModel = this.userRepository.findByUserName(dto.getUserName());
        UserModel user = null;
        if (userModel.isPresent()) {
            if(userModel.get().getRole().equals(Role.USER))
            {
                throw new RuntimeException("User is not Authorized User.!");
            }
            user=userModel.get();
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword()));
            log.info("autehntication {}", authentication.getPrincipal().toString());
            final String token = jwtService.generateToken(user);
            log.info("jwt token {}", token);
            UserTokenModel userToken =this.tokenRespository.findByUser(user);
            if(userToken==null)
            {
                userToken =new UserTokenModel();
                userToken.setToken(token);
                userToken.setUser(user);
                userToken.setCreatedDate(new Date());
                this.tokenRespository.save(userToken);
            }
            else{
                userToken.setToken(token);
                userToken.setModifiedDate(new Date());
                this.tokenRespository.save(userToken);
            }
            if (token != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Cookie cookie = new Cookie(Common.SESSION_ADMIN, token);
                cookie.setMaxAge(Common.COOKIES_EXPIRED);
                cookie.setPath("/");
                response.addCookie(cookie);
                return AuthenticationResponse.builder().token(token).userName(user.getUsername()).build();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("User Not Found..!");
        }
    }

    @Override
    public void logout(String userName) {
        Optional<UserModel> userModel = this.userRepository.findByUserName(userName);
        UserModel user = null;
        if (userModel.isPresent()) {
            user=userModel.get();
            UserTokenModel userToken = this.tokenRespository.findByUser(user);
            if (userToken!=null)
            {
                this.tokenRespository.delete(userToken);
            }
            SecurityContext context = SecurityContextHolder.getContext();
            SecurityContextHolder.clearContext();
            context.setAuthentication(null);
        }


    }
}
