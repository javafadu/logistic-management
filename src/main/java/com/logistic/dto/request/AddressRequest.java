package com.logistic.dto.request;


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
public class AddressRequest {


    private String type;

    @Size(max=180)
    @NotBlank(message = "Please provide a country name")
    private String country;

    private String state;

    @Size(max=180)
    @NotBlank(message = "Please provide a city name")
    private String city;

    private String district;

    @Size(max=22)
    @NotBlank(message = "Please provide a zip code")
    private String zipCode;

    private String address;

    private Double latitude;
    private Double longitude;


}
