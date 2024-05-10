package com.resturant.management.service;

import com.resturant.management.dto.CategoryDto;

import javax.servlet.http.HttpServletRequest;

public interface CategoryService {
    void addCategory(CategoryDto dto);

    Object reteriveCategories();

    Object reteriveCategoryById(Long id);

    void updateCategory(Long id, CategoryDto dto);

    void removeCategory(Long id);
}
