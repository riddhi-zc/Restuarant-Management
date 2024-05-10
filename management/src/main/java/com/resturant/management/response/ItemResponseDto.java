package com.resturant.management.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemResponseDto {

    private String item;
    private String description;
    private Double price;
    private byte[] menuImage;
    private String menuImageName;
    private Long id;
}
