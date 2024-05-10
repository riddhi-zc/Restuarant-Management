package com.resturant.management.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MenuDto {

    @NotNull(message = "Please Enter the menu item")
    private String item;

    @NotNull(message = "Please Enter the item price")
    private  Double price;

    @NotNull(message = "Please Enter category of item")
    private List<String> categoryName;

    private String description;

    private MultipartFile menuImage;

    private  String menuImageName;
}
