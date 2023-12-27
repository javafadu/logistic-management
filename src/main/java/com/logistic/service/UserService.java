package com.logistic.service;

import com.logistic.domain.User;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_EXCEPTION,email)));

        return user;
    }


}
