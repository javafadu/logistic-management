package com.logistic.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.logistic.domain.Address;
import com.logistic.domain.Company;
import com.logistic.domain.ImageFile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomerDTO {

    private Long id;

    @Size(max=180, message="size is exceeded")
    @NotBlank(message = "Please provide the name")
    private String name;


    private String category;

    private List<Long> addresses;

    private Long company;

    private Set<String> images;

}
