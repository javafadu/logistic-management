package com.logistic.service;

import com.logistic.domain.Role;
import com.logistic.domain.enums.RoleType;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {


    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role findByType(RoleType roleType) {
        Role role = roleRepository.findByType(roleType).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND_EXCEPTION,roleType.name())));

        return role;
    }


}
