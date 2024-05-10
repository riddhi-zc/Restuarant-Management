package com.resturant.management.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Entity
public class CategoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String categoryName;

    @ManyToMany(cascade =CascadeType.REMOVE)
    @JoinColumn(name = "menu_id")
    Set<MenuModel> menus;

    private Date creaatedDate;

    private Date modifiedDate;

}

