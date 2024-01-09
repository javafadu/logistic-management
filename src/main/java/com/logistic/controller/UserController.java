package com.logistic.controller;

import com.logistic.dto.UserDTO;
import com.logistic.dto.request.UpdatePasswordRequest;
import com.logistic.dto.request.UserUpdateRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.LoginResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get All Users
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    // Get Authenticated User Info (Logged in User)
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getAuthUserInfo() {
        UserDTO userDTO = userService.getPrincipal();
        return ResponseEntity.ok(userDTO);
    }


    // Get All Users with Paging
    @GetMapping("/all/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsersWithPaging(
            @RequestParam(required = false, value = "q", defaultValue = "") String q,
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size,
            @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
            @RequestParam(required = false, value = "direction", defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<UserDTO> userDTOPage = userService.getAllUsersWithPaging(q, pageable);
        return ResponseEntity.ok(userDTOPage);
    }



    // Get User by Id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {

        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    // Update password
    @PatchMapping("/auth")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        userService.updatePassword(updatePasswordRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setMessage(ResponseMessage.PASSWORD_UPDATE_RESPONSE_MESSAGE);
        logiResponse.setSuccess(true);

        return ResponseEntity.ok(logiResponse);
    }


    // Update User
    @PutMapping("/auth")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.USER_UPDATE_RESPONSE_MESSAGE);

        return ResponseEntity.ok(logiResponse);
    }


}
