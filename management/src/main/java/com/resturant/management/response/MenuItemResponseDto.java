package com.resturant.management.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MenuItemResponseDto {

    private String menuItem;

    private byte[] menuImage;

    private String menuImageName;

    private Double price;

    private String description;

    private Set<String> categoryName;

    private Long id;

}
