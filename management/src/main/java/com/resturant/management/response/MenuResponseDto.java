package com.resturant.management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponseDto {
    private String categories;
    private List<ItemResponseDto> itemList;
}
