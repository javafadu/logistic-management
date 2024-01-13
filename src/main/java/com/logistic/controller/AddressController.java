package com.logistic.controller;

import com.logistic.dto.request.AddressRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    // user add his/her address
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> userAddOwnAddress(@Valid @RequestBody AddressRequest addressRequest) {
        addressService.addOwnAddress(addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.ADDRESS_ADDED_RESPONSE_MESSAGE);
        return ResponseEntity.ok(logiResponse);
    }

    // Admin add an address for a user
    @PostMapping("/add/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> adminAddUserAddress(@PathVariable("id") Long userId, @Valid @RequestBody AddressRequest addressRequest) {
        addressService.addAddressForUser(userId, addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setMessage(ResponseMessage.ADDRESS_ADDED_RESPONSE_MESSAGE);
        logiResponse.setSuccess(true);
        return ResponseEntity.ok(logiResponse);
    }

    // User Update own address
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> updateOwnAddress(@PathVariable("id") Long addressId,@Valid @RequestBody AddressRequest addressRequest) {
        addressService.updateOwnAddress(addressId, addressRequest);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.ADDRESS_UPDATED_RESPONSE_MESSAGE);
        return ResponseEntity.ok(logiResponse);
    }

}