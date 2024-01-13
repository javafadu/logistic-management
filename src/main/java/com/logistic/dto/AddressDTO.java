package com.logistic.dto;

import com.logistic.domain.Location;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {


    private Long id;

    private String type;

    private String country;

    private String state;

    private String city;

    private String district;

    private String zipCode;

    private String address;

    private Location location;

}
