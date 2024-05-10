package com.resturant.management.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String orderId;

    @OneToMany( targetEntity = OrderItemQuantitiesModel.class,mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItemQuantitiesModel> quantities = new ArrayList<>();

    private Double totalPrice;

    private Date createdDate;

    private Date modifiedDate;
}
