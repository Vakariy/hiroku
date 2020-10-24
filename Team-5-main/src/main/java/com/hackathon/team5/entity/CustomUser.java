package com.hackathon.team5.entity;


import com.hackathon.team5.enums.Role;
import lombok.Data;


import javax.persistence.*;

@Data
@Entity
public class CustomUser {
    @Id
    @GeneratedValue
    private Long idUser;
    private String firstName;
    private String name;
    private String surName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String uuid;

    @Enumerated(EnumType.STRING)
    private Role role;

    public CustomUser() {
    }

    public CustomUser(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role=role;
    }

    public CustomUser(String firstName, String name, String surName, String email, String password, Role role) {
        this.firstName = firstName;
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.password = password;
        this.role=role;
    }
}
