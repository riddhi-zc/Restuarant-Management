package com.resturant.management.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {

    private Long quantity;
    private String  itemName;
    private Double price;
    private Double totalPrice;
    private Long menuId;
    private String category;




}
