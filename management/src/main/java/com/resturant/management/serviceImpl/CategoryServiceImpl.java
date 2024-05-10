package com.resturant.management.serviceImpl;

import com.resturant.management.constants.ErrorConstants;
import com.resturant.management.dto.CategoryDto;
import com.resturant.management.entity.CategoryModel;
import com.resturant.management.entity.MenuModel;
import com.resturant.management.exception.NotFoundException;
import com.resturant.management.repository.CategoryRepository;
import com.resturant.management.repository.MenuRepository;
import com.resturant.management.response.CategoryResponseDto;
import com.resturant.management.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepo;

    @Autowired
    MenuRepository menuRepo;

    @Override
    public void removeCategory(Long id) {
        try {
            Optional<CategoryModel> categoryModel = this.categoryRepo.findById(id);
            if (categoryModel.isPresent()) {
                List<MenuModel> menuModels = this.menuRepo.findByCategoryName(categoryModel.get());
                if (menuModels.size() > 0) {
                    this.menuRepo.deleteAll(menuModels);

                }
                this.categoryRepo.delete(categoryModel.get());
            } else {
                throw new NotFoundException(ErrorConstants.CATEGORY_NOT_FOUND);
            }
        } finally {

        }
    }

    @Override
    public Object reteriveCategories() {
        List<CategoryResponseDto> response = new ArrayList<>();
        try {
            List<CategoryModel> categories = this.categoryRepo.findAll();
            if (categories.size() > 0) {
                for (CategoryModel category : categories) {
                    response.add(CategoryResponseDto.builder()
                            .categoryNames(category.getCategoryName())
                            .cid(category.getId())
                            .build());
                }
            }
        } finally {

        }
        return response;
    }

    @Override
    public Object reteriveCategoryById(Long id) {
        List<CategoryResponseDto> response = new ArrayList<>();

        Optional<CategoryModel> category = this.categoryRepo.findById(id);
        if (category.isPresent()) {
            response.add(CategoryResponseDto.builder()
                    .categoryNames(category.get().getCategoryName())
                    .cid(category.get().getId())
                    .build());
            return response;
        } else {
            return  new NotFoundException(ErrorConstants.CATEGORY_NOT_FOUND);
        }

    }

    @Override
    public void updateCategory(Long id, CategoryDto dto) {
        Optional<CategoryModel> category = this.categoryRepo.findById(id);
        if (category.isPresent()) {
            CategoryModel model=category.get();
            model.setCategoryName(dto.getCategory());
            model.setModifiedDate(new Date());
            this.categoryRepo.save(model);
        } else {
            throw new NotFoundException(ErrorConstants.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void addCategory(CategoryDto dto) {
        try {
            Optional<CategoryModel> categories = this.categoryRepo.findByCategoryName(dto.getCategory());
            if (!categories.isPresent()) {
                CategoryModel categoryModel = new CategoryModel();
                categoryModel.setCategoryName(dto.getCategory());
                categoryModel.setCreaatedDate(new Date());
                this.categoryRepo.save(categoryModel);
            } else {
                throw new RuntimeException(ErrorConstants.MENU_ALREADY_EXISTS);
            }
        } finally {
        }

    }
}
