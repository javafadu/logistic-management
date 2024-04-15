package com.logistic.controller;

import com.logistic.dto.CompanyDTO;
import com.logistic.dto.request.AddressRequest;
import com.logistic.dto.response.LogiResponse;
import com.logistic.dto.response.ResponseMessage;
import com.logistic.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController
{
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // Add Company by Admin
    @PostMapping("/add")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> companyAdd(@Valid @RequestBody CompanyDTO companyDTO) {
        companyService.companyAdd(companyDTO);
        LogiResponse response = new LogiResponse();
        response.setMessage(ResponseMessage.COMPANY_ADDED_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // Update Company
    @PutMapping("/update/{companyId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<LogiResponse> updateCompany(@PathVariable("companyId") Long companyID, @Valid @RequestBody CompanyDTO companyDTO) {
        companyService.updateCompany(companyID, companyDTO);
        LogiResponse logiResponse = new LogiResponse();
        logiResponse.setSuccess(true);
        logiResponse.setMessage(ResponseMessage.COMPANY_UPDATED_RESPONSE_MESSAGE);
        return ResponseEntity.ok(logiResponse);
    }



}
