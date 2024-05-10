package com.resturant.management.response;

import lombok.Data;

@Data
public class DataTableResponseDto {
    private Integer totalRecords;
    private Integer fliterRecords;
    private Object response;
}
