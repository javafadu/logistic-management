package com.logistic.controller;

import com.logistic.dto.request.UserRegisterRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.security.jwt.JwtUtils;
import com.logistic.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserJwtController {

    @Autowired
    private  JwtUtils jwtUtils;
    @Autowired
    private  UserService userService;
    @Autowired
    private  AuthenticationManager authenticationManager;




    // REGISTER A USER by public
    @PostMapping("/register")
    public ResponseEntity<LogiResponse> registerUser(@Valid @RequestBody UserRegisterRequest registerRequest) {

        userService.saveUser(registerRequest);
        LogiResponse response = new LogiResponse();
                response.setMessage(ResponseMessage.USER_REGISTER_RESPONSE_MESSAGE);
                response.setSuccess(true);
       return new ResponseEntity<>(response, HttpStatus.CREATED);

    }


}
