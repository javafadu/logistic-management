package com.logistic.domain.enums;

public enum RoleType {

    // The role of any registered user should be BASIC as default
    ROLE_USER("User"),
    ROLE_CUSTOMER("Customer"),
    ROLE_SUPPLIER("Supplier"),
    ROLE_MANAGER("Manager"),
    ROLE_ADMIN("Administrator");

    private String name;

    private RoleType(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

}
