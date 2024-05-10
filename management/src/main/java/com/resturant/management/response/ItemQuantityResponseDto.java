package com.resturant.management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemQuantityResponseDto {

    private String item;

    private Double price;

    private Long quantity;
}
