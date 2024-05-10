package com.resturant.management.response;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseMessageDto {

    private Boolean isErrorMessage;
    private String errorMessage;
    private Object response;

}
