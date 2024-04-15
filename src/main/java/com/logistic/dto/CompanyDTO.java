package com.logistic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    @Size(max=180)
    @NotBlank(message = "Please provide a company name")
    private String name;

    private String taxOffice;

    private String taxNo;

    private String phone;

    private String email;

    private String webSite;

}
