package com.logistic.service;

import com.logistic.domain.Role;
import com.logistic.domain.User;
import com.logistic.domain.enums.RoleType;
import com.logistic.dto.mapper.UserMapper;
import com.logistic.dto.request.UserRegisterRequest;
import com.logistic.dto.response.UserResponse;
import com.logistic.exception.ConflictException;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_EXCEPTION, email)));

        return user;
    }


    // Register a user by public
    public void saveUser(UserRegisterRequest registerRequest) {
        // check1: control e-mail is exist or not
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_ERROR_MESSAGE, registerRequest.getEmail()));
        }

        // Check2: set the role as User
        Role role = roleService.findByType(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Check3: encode the string password before saving in the DB
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // SET all related fields and save
        LocalDateTime today = LocalDateTime.now();
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setBirthDate(registerRequest.getBirthDate());
        user.setStatus("active");
        user.setPhone(registerRequest.getPhone());
        user.setRegisterDate(today);
        user.setRoles(roles);

        userRepository.save(user);
    }
}
