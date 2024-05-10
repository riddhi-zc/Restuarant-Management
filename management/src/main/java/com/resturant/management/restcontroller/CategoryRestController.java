package com.resturant.management.restcontroller;

import com.resturant.management.constants.Common;
import com.resturant.management.constants.ErrorConstants;
import com.resturant.management.dto.CategoryDto;
import com.resturant.management.dto.ErrorDto;
import com.resturant.management.entity.Role;
import com.resturant.management.entity.UserModel;
import com.resturant.management.exception.ForbiddenHandleException;
import com.resturant.management.response.MenuResponseDto;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.CategoryService;
import com.resturant.management.service.MenuService;
import com.resturant.management.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@RestController
@RequestMapping("/api/category")
public class CategoryRestController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @GetMapping("/getCategories")
    private ResponseMessageDto reteriveCategories(Principal principal,HttpServletRequest request) {
        try {

            UserModel user = null;
            user = this.userService.getUserData(principal);
            if (user != null) {
                return new ResponseMessageDto(Boolean.TRUE, null, this.categoryService.reteriveCategories());
            }
            else {
                throw  new ForbiddenHandleException("Unauthorized Request");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally {
        }

    }
    @GetMapping("/getCategory/{id}")
    private ResponseMessageDto reteriveCategoryById(Principal principal,HttpServletRequest request, @PathVariable("id")Long id) {
        try {
            UserModel user = null;
            user = this.userService.getUserData(principal);
            if (user != null) {
                if(user.getRole().equals(Role.ADMIN)) {
                    return new ResponseMessageDto(Boolean.TRUE, null, this.categoryService.reteriveCategoryById(id));
                }
                else
                {
                    throw  new ForbiddenHandleException("User is not Admin User");
                }

            }
            else {
                throw  new ForbiddenHandleException("Unauthorized Request");
            }
        }  catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500, ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally {
        }

    }
    @PostMapping("/addCategory")
    private ResponseMessageDto addMenu(Principal principal,HttpServletRequest request, @RequestBody CategoryDto dto)
    {
        try{
            UserModel user = null;
            user = this.userService.getUserData(principal);
            if (user != null) {
                if(user.getRole().equals(Role.ADMIN)) {
                    this.categoryService.addCategory(dto);
                    return new ResponseMessageDto(Boolean.TRUE, null, Common.CATEGORY_ADDED);
                }
                else {
                    throw new ForbiddenHandleException("User is not an Admin User");
                }
            }
            else
            {
                throw  new ForbiddenHandleException("Unauthorized Request");
            }
        }
        finally
        {}

    }

    @DeleteMapping("/removeCategory/{id}")
    private ResponseMessageDto removeCategory(Principal principal,HttpServletRequest request, @PathVariable(name = "id")Long id)
    {
        UserModel user = null;
        user = this.userService.getUserData(principal);
        try{

            if (user != null) {
                if(user.getRole().equals(Role.ADMIN)) {
                    this.categoryService.removeCategory(id);
                    return new ResponseMessageDto(Boolean.TRUE, null, Common.CATEGORY_DELETED);
                }
                else
                {
                    throw  new ForbiddenHandleException("User is not an Admin User");
                }
            }
            else
            {
                throw new ForbiddenHandleException("Unauthorized Exception");
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500,ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.CATEGORY_DELETED);
        }
        finally
        {}

    }

    @PutMapping("/updateCategory/{id}")
    private ResponseMessageDto  updateCategory(Principal principal,HttpServletRequest request, @PathVariable(name = "id")Long id,@RequestBody CategoryDto dto)
    {
        try{
            UserModel user = null;
            user = this.userService.getUserData(principal);
            if (user != null) {
                if(user.getRole().equals(Role.ADMIN)) {
                    this.categoryService.updateCategory(id, dto);
                    return new ResponseMessageDto(Boolean.TRUE, null, Common.CATEGORY_UPDATED);
                }
                else
                {
                    throw new ForbiddenHandleException("User is not admin user");
                }
            }
            else {
                throw new ForbiddenHandleException("Unauthorized Request");
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
            return   new ResponseMessageDto(Boolean.FALSE,new ErrorDto(500,ErrorConstants.INTERNAL_SERVER_ERROR).getErrorMessage(), Common.MENU_DELETED);
        }
        finally
        {}

    }

}
