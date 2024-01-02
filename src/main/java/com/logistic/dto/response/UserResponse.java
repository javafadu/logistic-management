package com.logistic.dto.response;

import com.logistic.domain.Role;
import com.logistic.domain.User;
import com.logistic.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private LocalDateTime registerDate;
    private String status;
    private Set<String> roles;


    // Converting Set<Role> roles in DB to Set<String> roles as dto
    public void setRoles(Set<Role> roles) {
        Set<String> rolesStr = new HashSet<>();

        for (Role r : roles
        ) {
            if (r.getType().equals(RoleType.ROLE_USER)) {
                rolesStr.add(RoleType.ROLE_USER.getName());
            } else if (r.getType().equals(RoleType.ROLE_CUSTOMER)) {
                rolesStr.add(RoleType.ROLE_CUSTOMER.getName());
            } else if (r.getType().equals(RoleType.ROLE_SUPPLIER)) {
                rolesStr.add(RoleType.ROLE_SUPPLIER.getName());
            }   else if (r.getType().equals(RoleType.ROLE_MANAGER)) {
            rolesStr.add(RoleType.ROLE_MANAGER.getName());
            }   else if (r.getType().equals(RoleType.ROLE_ADMIN)) {
            rolesStr.add(RoleType.ROLE_ADMIN.getName());
            }
            else {
                rolesStr.add("");
            }
        }
        this.roles = rolesStr;
    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.birthDate = user.getBirthDate();
        this.registerDate = user.getRegisterDate();
        this.status =user.getStatus();
        this.roles = getRoles();
        //  Set<Role> convert to Set<String> with above method
    }


}
