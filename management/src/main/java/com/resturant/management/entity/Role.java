package com.resturant.management.entity;

public enum Role {
    ADMIN,USER;
    public Role parseRole(String roleValue) {
        try {
            return Role.valueOf(roleValue);
        } catch (IllegalArgumentException e) {
            // Handle unknown role values
            throw new IllegalArgumentException("Unknown role value: " + roleValue);
        }
    }
}
