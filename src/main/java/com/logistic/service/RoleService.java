package com.logistic.service;

import com.logistic.domain.Role;
import com.logistic.domain.enums.RoleType;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {


    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<String> addRoles() {
        // create a blank list to be added already exist roles in DB
        List<String> existRolesList = new ArrayList<>();

        for (RoleType each : RoleType.values()
        ) {
            if (!roleRepository.existsByType(each)) {
                Role role = new Role();
                role.setType(each);
                roleRepository.save(role);
            } else {
                existRolesList.add(each.name());
            }
        }
        return existRolesList;
    }

    public Boolean existsByType(RoleType type) {
        return roleRepository.existsByType(type);
    }


    public Role findByType(RoleType roleType) {
        Role role = roleRepository.findByType(roleType).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND_EXCEPTION,roleType.name())));

        return role;
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }


}
