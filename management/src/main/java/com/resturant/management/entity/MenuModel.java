package com.resturant.management.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class MenuModel {


        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToMany(targetEntity = CategoryModel.class, fetch = FetchType.LAZY)
        private Set<CategoryModel> categoryName;

        @ManyToMany(targetEntity = OrderModel.class,fetch = FetchType.LAZY)
        private List<OrderModel> orders;

        private String item;

        private String description;

        private Double price;

        @Lob
        @Basic(fetch=FetchType.LAZY)
        @Column(name = "menu_image")
        @Type(type = "org.hibernate.type.BinaryType")
        private byte[] menuImage;

        private  String menuImageName;

        private Date creaatedDate;

        private Date modifiedDate;


}
