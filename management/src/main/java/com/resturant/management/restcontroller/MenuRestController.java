package com.resturant.management.restcontroller;

import com.resturant.management.constants.Common;
import com.resturant.management.constants.ErrorConstants;
import com.resturant.management.dto.ErrorDto;
import com.resturant.management.dto.MenuDto;
import com.resturant.management.entity.Role;
import com.resturant.management.entity.UserModel;
import com.resturant.management.exception.ForbiddenHandleException;
import com.resturant.management.repository.UserRepository;
import com.resturant.management.response.ResponseMessageDto;
import com.resturant.management.service.MenuService;
import com.resturant.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/menu")
public class MenuRestController {

    @Autowired
    UserService userService;



    @Autowired
    MenuService menuService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/addMenu")
    private ResponseMessageDto addMenu(Principal principal,HttpServletRequest request, @ModelAttribute MenuDto dto) {
        try {
            log.info("principal{}",principal.getName());
            UserModel user = null;

            user = this.userService.getUserData(principal);
            if (user != null) {
                if(user.getRole().equals(Role.ADMIN)) {
                    this.menuService.addMenu(dto);
                    return new ResponseMessageDto(Boolean.TRUE, null, Common.MENU_ADDED);
                }
                else
                {
                    throw new ForbiddenHandleException("User is not Admin User");
                }
            }
            else
            {
                throw  new ForbiddenHandleException("Unauthorized Request");
            }
        } finally {

        }
    }

    @GetMapping("/getMenuByCategories")
    private ResponseMessageDto getMenuByCategories(Principal principal,HttpServletRequest request,@RequestParam(name = "pageNo")Integer pageNo,@RequestParam(name = "pageSize")Integer pageSize) {
        try {
            log.info("principal{}", principal.getName());
            UserModel user = null;
            user = this.userService.getUserData(principal);
            if (user != null) {
                if (user.getRole().equals(Role.USER))
                {return new ResponseMessageDto(Boolean.TRUE, null, this.menuService.getMenuByCategories(pageNo, pageSize));}
                else
                { throw  new ForbiddenHandleException("User have not Access");}
            } else {
                throw new ForbiddenHandleException("Unauthorized Request");
            }
        }
                finally {
        }
    }

    @GetMapping("/getMenuByCategoryId/{cid}")
    private ResponseMessageDto getMenuByCategory(Principal principal,HttpServletRequest request, @PathVariable("cid") Long cid) {
        try {
            log.info("principal{}",principal.getName());
            UserModel user = null;

            user = this.userService.getUserData(principal);
            if (user != null) {

                return new ResponseMessageDto(Boolean.TRUE, null, this.menuService.getMenuByCategory(cid));
            }
            else
            {
                    throw new ForbiddenHandleException("Unauthorized Request");
            }

        } finally {
        }

    }


    @GetMapping("/getMenus")
    private ResponseMessageDto getMenus(Principal principal,HttpServletRequest request, @RequestParam(name = "pageNo", required = false) Integer pageNo, @RequestParam(name = "pageSize", required = false) Integer pageSize, @RequestParam(name = "search", required = false) Object search) {
        try {
            log.info("principal{}",principal.getName());
            UserModel user = null;

            user = this.userService.getUserData(principal);
            if (user != null) {
                if(user.getRole().equals(Role.ADMIN))
                {return new ResponseMessageDto(Boolean.TRUE, null, this.menuService.getMenus(pageNo, pageSize, search));}
                else
                {throw  new ForbiddenHandleException("User is not an Admin User");}

            }
            else {
                throw  new ForbiddenHandleException("Unauthorized Request");
            }


        } finally {
        }

    }


    @DeleteMapping("/removeMenu/{id}")
    private ResponseMessageDto removeMenu(Principal principal,HttpServletRequest request, @PathVariable(name = "id") Long id) {
        try {
            log.info("principal{}",principal.getName());
            UserModel user = null;
            user = this.userService.getUserData(principal);
            if (user != null) {
                if (user.getRole().equals(Role.ADMIN)) {
                    this.menuService.removeItemFromMenu(id);
                    return new ResponseMessageDto(Boolean.TRUE, null, Common.MENU_DELETED);
                }
                else {
                    throw  new ForbiddenHandleException("User is not Admin User.!");
                }
            }
            else {
                 throw  new ForbiddenHandleException("Unauthorized Request");
            }
        } finally {
        }

    }


    @PutMapping("/updateMenu/{menuId}")
    private ResponseMessageDto updateMenu(Principal principal,HttpServletRequest request, @PathVariable(name = "menuId") Long menuId, @RequestParam(name = "menuImage", required = false) MultipartFile menuImageFile, @RequestParam(name = "menuImageFileName", required = false) String menuImageFileName, @RequestParam(name = "item") String item, @RequestParam(name = "description") String description, @RequestParam(name = "categoryName") List<String> categoryName, @RequestParam(name = "price") Double price) {
        try {

            log.info("principal{}",principal.getName());
            UserModel user = null;

            user = this.userService.getUserData(principal);
            if (user != null) {
                if (user.getRole().equals(Role.ADMIN)) {
                MenuDto dto = new MenuDto();
                dto.setItem(item);
                dto.setMenuImage(menuImageFile);
                dto.setMenuImageName(menuImageFileName);
                dto.setDescription(description);
                dto.setPrice(price);
                dto.setCategoryName(categoryName);
                log.info("Item: {} Description: {}", item, description);
                this.menuService.updateMenu(menuId, dto);
                return new ResponseMessageDto(Boolean.TRUE, null, Common.MENU_UPDATED);
            }
                else
                {throw  new ForbiddenHandleException("User is not Admin user");}
            }
            else {
                throw  new ForbiddenHandleException("Unauthorized Request");
            }
        } finally {
        }

    }

}
