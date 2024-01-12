package com.logistic.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.logistic.domain.Address;
import com.logistic.domain.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    // Convert USER entity coming from DB to DTO


    private Long id;

    private String name;

    private String email;

    private String phone;

    private LocalDate birthDate;

    private List<Address> addresses;

    private String resetPasswordCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
            "yyyy-MM-dd HH:mm:ss", timezone = "Turkey")
    private LocalDateTime registerDate;

    private String status;

    private Set<String> roles;


    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();

        roles.forEach(r-> {
            roleStr.add(r.getType().getName()); // User, Supplier, Manager...
                }
        );
        this.roles=roleStr;
    }

}
