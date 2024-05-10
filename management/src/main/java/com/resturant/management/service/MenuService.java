package com.resturant.management.service;


import com.resturant.management.dto.MenuDto;
import com.resturant.management.entity.UserModel;
import com.resturant.management.response.MenuResponseDto;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

public interface MenuService {

    List<MenuResponseDto> getMenuByCategories(Integer pageNo,Integer pageSize);

    void addMenu(MenuDto dto);

    void removeItemFromMenu(Long id);

    void updateMenu(Long id, MenuDto dto);

    Object getMenus(Integer pageNo, Integer pageSize, Object search);

    Object getMenuByCategory(Long cid);


}
