package com.resturant.management.repository;

import com.resturant.management.entity.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository  extends JpaRepository<CategoryModel, Long> {

    @Query("SELECT category FROM CategoryModel  AS category WHERE category.categoryName IN :categoryName")
    List<CategoryModel> findByCategoryName(@Param("categoryName") List<String> categoryName);

    @Query("SELECT category FROM CategoryModel  AS category WHERE category.categoryName = :categoryName")
    Optional<CategoryModel> findByCategoryName(@Param("categoryName") String category);
}
