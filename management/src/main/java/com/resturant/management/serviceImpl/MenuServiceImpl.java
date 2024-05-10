package com.resturant.management.serviceImpl;


import com.resturant.management.constants.ErrorConstants;

import com.resturant.management.dto.MenuDto;
import com.resturant.management.entity.CategoryModel;
import com.resturant.management.entity.MenuModel;
import com.resturant.management.exception.NotFoundException;
import com.resturant.management.repository.CategoryRepository;
import com.resturant.management.repository.MenuRepository;
import com.resturant.management.response.DataTableResponseDto;
import com.resturant.management.response.ItemResponseDto;
import com.resturant.management.response.MenuItemResponseDto;
import com.resturant.management.response.MenuResponseDto;
import com.resturant.management.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    MenuRepository menuRepo;

    @Override
    public List<MenuResponseDto> getMenuByCategories(Integer pageNo, Integer pageSize) {
        List<MenuResponseDto> response = new ArrayList<>();
        List<MenuModel> menus = this.menuRepo.findAll();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<MenuModel> menuPage = menuRepo.findAll(pageable);

        Map<String, List<MenuModel>> menusGrouped = menuPage.stream()
                .flatMap(menu -> menu.getCategoryName().stream()
                        .map(category -> new AbstractMap.SimpleEntry<>(category.getCategoryName(), menu)))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
        for (Map.Entry<String, List<MenuModel>> entry : menusGrouped.entrySet()) {
            MenuResponseDto res = new MenuResponseDto();
            res.setCategories(entry.getKey());
            res.setItemList(buildResponse(entry.getValue()));
            response.add(res);
        }

        return response;
    }

    private Set<String> buildKeyCategoryResponse(Set<CategoryModel> key) {
        Set<String> categoryName = new HashSet<>();
        for (CategoryModel cat : key) {
            categoryName.add(cat.getCategoryName());
        }
        return categoryName;
    }

    private List<ItemResponseDto> buildResponse(List<MenuModel> value) {
        List<ItemResponseDto> itemList = new ArrayList<>();
        for (MenuModel m : value) {
            itemList.add(ItemResponseDto.builder().item(m.getItem())
                    .price(m.getPrice()).description(m.getDescription()).id(m.getId())
                    .menuImage(m.getMenuImageName().getBytes())
                    .menuImageName(m.getMenuImageName())
                    .build());
        }
        return itemList;
    }


    @Override
    @Transactional
    public void addMenu(MenuDto dto) {
        try {
            List<CategoryModel> categoryModel = this.categoryRepo.findByCategoryName(dto.getCategoryName());
            if (categoryModel.size() > 0) {
                MenuModel menuModel = new MenuModel();
                Set<CategoryModel> categories = new HashSet<>();
                categories.addAll(categoryModel);
                menuModel.setCategoryName(categories);
                menuModel.setItem(dto.getItem());
                menuModel.setPrice(dto.getPrice());
                menuModel.setMenuImageName(dto.getMenuImageName());
                byte[] imageData;
                imageData = dto.getMenuImage().getBytes();
                menuModel.setMenuImage(imageData);
                menuModel.setCreaatedDate(new Date());
                menuModel.setDescription(dto.getDescription());
                this.menuRepo.save(menuModel);
            } else throw new NotFoundException(ErrorConstants.MENU_NOT_FOUND);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


    @Override
    @Transactional
    public void removeItemFromMenu(Long id) {

        Optional<MenuModel> menus = this.menuRepo.findById(id);
        if (menus.isPresent()) {
            this.menuRepo.delete(menus.get());
        } else {
            throw new NotFoundException(ErrorConstants.MENU_NOT_FOUND);
        }

    }

    @Override
    public void updateMenu(Long id, MenuDto dto) {
        try {
            Optional<MenuModel> menus = this.menuRepo.findById(id);
            if (menus.isPresent()) {
                MenuModel menu = menus.get();
                menu.setDescription(dto.getDescription());
                menu.setItem(dto.getItem());
                menu.setPrice(dto.getPrice());
                List<CategoryModel> categoryModel = this.categoryRepo.findByCategoryName(dto.getCategoryName());
                if (categoryModel.size() > 0) {
                    Set<CategoryModel> categoryModels = new HashSet<>();
                    categoryModels.addAll(categoryModel);
                    menu.setCategoryName(categoryModels);
                }
                byte[] imageData;
                if (dto.getMenuImage() != null && dto.getMenuImageName() != null) {
                    imageData = dto.getMenuImage().getBytes();
                    menu.setMenuImage(imageData);
                    menu.setMenuImageName(dto.getMenuImageName());
                }
                menu.setModifiedDate(new Date());
                this.menuRepo.save(menus.get());
            } else {
                throw new NotFoundException(ErrorConstants.MENU_NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }


    @Override
    public Object getMenus(Integer pageNo, Integer pageSize, Object search) {
        DataTableResponseDto  res=new DataTableResponseDto();
        List<MenuItemResponseDto> response = new ArrayList<>();
        if (search != null) {
            if (pageNo != null && pageSize != null) {
                Page<MenuModel> pageMenu = this.menuRepo.findByPageSearch(PageRequest.of(pageNo, pageSize), search);
                if (pageMenu != null && pageMenu.getSize() > 0) {
                    for (MenuModel menu : pageMenu.getContent()) {
                        response.add(buildMenuResponse(menu));
                    }
                }
                res.setFliterRecords( pageMenu.stream().collect(Collectors.toList()).size());
            } else {
                Page<MenuModel> pageMenu = this.menuRepo.findByPageSearch(PageRequest.of(0, 10), search);
                if (pageMenu != null && pageMenu.getSize() > 0) {
                    for (MenuModel menu : pageMenu.getContent()) {
                        response.add(buildMenuResponse(menu));
                    }
                }
                res.setFliterRecords( pageMenu.getSize());
            }
            res.setTotalRecords(this.menuRepo.findAll().size());
            res.setResponse(response);

        } else {
            if (pageNo != null && pageSize != null) {
                Page<MenuModel> pageMenu = this.menuRepo.findByPage(PageRequest.of(pageNo, pageSize));
                if (pageMenu != null && pageMenu.getSize() > 0) {
                    for (MenuModel menu : pageMenu.getContent()) {
                        response.add(buildMenuResponse(menu));
                    }
                }
                res.setFliterRecords( pageMenu.getSize());
            } else {
                Page<MenuModel> pageMenu = this.menuRepo.findByPage(PageRequest.of(0, 10));
                if (pageMenu != null && pageMenu.getSize() > 0) {
                    for (MenuModel menu : pageMenu.getContent()) {
                        response.add(buildMenuResponse(menu));
                    }
                }
                res.setFliterRecords( pageMenu.getSize());
            }
            res.setTotalRecords(this.menuRepo.findAll().size());
            res.setResponse(response);
        }
        return res;
    }

    @Override
    public Object getMenuByCategory(Long cid) {
        MenuResponseDto response = new MenuResponseDto();
        Optional<CategoryModel> categoryModel = this.categoryRepo.findById(cid);
        if (categoryModel.isPresent()) {
            List<MenuModel> menus = this.menuRepo.findByCategoryName(categoryModel.get());
            List<ItemResponseDto> items = new ArrayList<>();
            items.addAll(buildResponse(menus));
            response.setCategories(categoryModel.get().getCategoryName());
            response.setItemList(items);
        }
        return response;
    }

    private MenuItemResponseDto buildMenuResponse(MenuModel menu) {
        return MenuItemResponseDto.builder()
                .categoryName(buildCategoryNamesResponse(menu.getCategoryName()))
                .description(menu.getDescription())
                .id(menu.getId())
                .price(menu.getPrice())
                .menuItem(menu.getItem()).menuImage(menu.getMenuImage()).menuImageName(menu.getMenuImageName())
                .build();
    }

    private Set<String> buildCategoryNamesResponse(Set<CategoryModel> categories) {
        Set<String> categoryValues = new HashSet<>();
        for (CategoryModel cat : categories) {
            categoryValues.add(cat.getCategoryName());
        }
        return categoryValues;
    }


}
