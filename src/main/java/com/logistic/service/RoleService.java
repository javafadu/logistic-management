package com.logistic.service;

import com.logistic.domain.Role;
import com.logistic.domain.enums.RoleType;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByType(RoleType roleType) {
        Role role = roleRepository.findByType(roleType).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.ROLE_NOT_FOUND_EXCEPTION,roleType.name())));

        return role;
    }

}
