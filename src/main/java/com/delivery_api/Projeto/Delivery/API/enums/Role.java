package com.delivery_api.Projeto.Delivery.API.enums;

public enum Role {
    ADMIN("ADMIN"),
    RESTAURANTE("RESTAURANTE"),
    CLIENTE("CLIENTE");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
