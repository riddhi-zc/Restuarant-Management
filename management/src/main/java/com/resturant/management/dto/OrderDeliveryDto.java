package com.resturant.management.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDeliveryDto {

    private Date deliveryTime;
    private String deliveryAddress;
    private String landmark;
    private String phoneNumber;
    private String name;

}
