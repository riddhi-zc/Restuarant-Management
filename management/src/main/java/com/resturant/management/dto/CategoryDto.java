package com.resturant.management.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {

    @NotNull(message = "Enter the catergory name")
    private String category;

}
