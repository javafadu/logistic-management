package com.logistic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="tbl_addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String type;

    @Column(length = 70)
    private String country;

    @Column(length = 70)
    private String state;

    @Column(length = 70)
    private String city;

    @Column(length = 100)
    private String district;

    @Column(length = 10)
    private String zipCode;

    @Column(length = 150)
    private String address;

    private Location location;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
