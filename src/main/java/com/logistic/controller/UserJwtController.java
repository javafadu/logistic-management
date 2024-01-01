package com.logistic.controller;

import com.logistic.dto.response.LogiResponse;
import com.logistic.security.jwt.JwtUtils;
import com.logistic.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserJwtController {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserJwtController(JwtUtils jwtUtils, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<LogiResponse> registerUser(@Valid ) {

    }


}
