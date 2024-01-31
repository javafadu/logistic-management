package com.logistic.dto;

import com.logistic.domain.Address;
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

public class ClientDTO {

    private Long id;

    @Size(max=180, message="size is exceeded")
    @NotBlank(message = "Please provide the name")
    private String name;

    @Size(max=80, message="size is exceeded")
    @NotBlank(message = "Please provide the type of client")
    private String type;

    private String category;

    private List<Long> addresses;

    private Long company;

    private Set<String> images;

}
