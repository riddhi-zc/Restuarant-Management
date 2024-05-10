package com.resturant.management.repository;

import com.resturant.management.entity.CategoryModel;
import com.resturant.management.entity.MenuModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MenuRepository extends JpaRepository<MenuModel,Long> {

    List<MenuModel> findByCategoryName(CategoryModel category);

   @Query("SELECT menus FROM MenuModel AS menus ORDER BY menus.id ASC")
    Page<MenuModel> findByPage(Pageable of);


    @Query("SELECT menus FROM MenuModel AS menus WHERE menus.description LIKE %:search% OR menus.item LIKE %:search%")
    Page<MenuModel> findByPageSearch(Pageable of, @Param("search") Object search);

    @Query("SELECT menus FROM MenuModel AS menus WHERE menus.item=:item AND menus.description=:description AND menus.price=:price")
    Optional<MenuModel> findByItem( @Param("item") String item, @Param("description") String description, @Param("price") Double price);

    @Query("SELECT menus FROM MenuModel AS menus WHERE menus.item=:item")
    MenuModel findByItemName(@Param("item") String itemName);
}
