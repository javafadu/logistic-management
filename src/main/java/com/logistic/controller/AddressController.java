package com.logistic.controller;

import com.logistic.dto.AddressDTO;
import com.logistic.dto.request.AddressRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    // user add his/her address
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LogiResponse> userAddOwnAddress(@Valid @RequestBody AddressRequest addressRequest) {
        addressService.addOwnAddress(addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.ADDRESS_ADDED_RESPONSE_MESSAGE);
        return ResponseEntity.ok(logiResponse);
    }

    // Admin or Manager add an address for a user
    @PostMapping("/add/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> adminAddUserAddress(@PathVariable("id") Long userId, @Valid @RequestBody AddressRequest addressRequest) {
        addressService.addAddressForUser(userId, addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setMessage(ResponseMessage.ADDRESS_ADDED_RESPONSE_MESSAGE);
        logiResponse.setSuccess(true);
        return ResponseEntity.ok(logiResponse);
    }

    // User Update own address
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LogiResponse> updateOwnAddress(@PathVariable("id") Long addressId,@Valid @RequestBody AddressRequest addressRequest) {
        addressService.updateOwnAddress(addressId, addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.ADDRESS_UPDATED_RESPONSE_MESSAGE);
        return ResponseEntity.ok(logiResponse);
    }

    // Admin or Manager Update address of a User
    @PutMapping("/update/{userId}/{addressId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> updateUserAddress(@PathVariable("userId") Long userId, @PathVariable("addressId") Long addressId, @Valid @RequestBody AddressRequest addressRequest) {
        addressService.updateUserAddress(userId, addressId, addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.ADDRESS_UPDATED_RESPONSE_MESSAGE);
        return ResponseEntity.ok(logiResponse);
    }

    // Get User Own Addresses
    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AddressDTO>> userAddresses() {
        List<AddressDTO> userAddresses = addressService.getUserOwnAddresses();
        return ResponseEntity.ok(userAddresses);
    }

    // Get Address of a user by Admin or Manager
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<AddressDTO>> userAddressesByAdmin(@PathVariable("userId") Long userId) {
        List<AddressDTO> userAddresses = addressService.getAddressesOfAUser(userId);
        return ResponseEntity.ok(userAddresses);
    }


    // Get an address in own Addresses
    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDTO> userAddress(@RequestParam("addressId") Long addressId) {
        AddressDTO userAddress = addressService.getUserOwnAddress(addressId);
        return ResponseEntity.ok(userAddress);
    }

    // Get an address of a user by Admin or Manager
    @GetMapping()
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<AddressDTO> userAddress(@RequestParam("userId") Long userId,@RequestParam("addressId") Long addressId) {
        AddressDTO userAddress = addressService.getUserAddress(userId,addressId);
        return ResponseEntity.ok(userAddress);
    }



}