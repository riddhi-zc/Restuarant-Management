package com.resturant.management.exception;

import org.springframework.stereotype.Component;


public class ForbiddenHandleException extends RuntimeException {
    public ForbiddenHandleException(String message){
        super(message);

    }
}
