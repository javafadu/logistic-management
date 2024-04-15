package com.logistic.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {


    @Size(max=180, message="size is exceeded")
    @NotBlank(message = "Please provide the name")
    private String name;

    @Size(max=80, message="size is exceeded")
    @NotBlank(message = "Please provide the type of supplier")
    private String type;

    private String category;



}
