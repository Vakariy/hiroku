package com.hackathon.team5.enums;

public enum Role {
    ADMIN,USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
