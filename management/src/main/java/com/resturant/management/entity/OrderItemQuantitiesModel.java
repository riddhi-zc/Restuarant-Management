package com.resturant.management.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItemQuantitiesModel {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @ManyToOne( targetEntity = OrderModel.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        private OrderModel order;

        @ManyToOne( targetEntity = MenuModel.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "menu_id")
        private MenuModel menu;

        private Long quantity;

}
