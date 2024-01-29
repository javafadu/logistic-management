package com.logistic.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

@Entity
@Table(name="tbl_customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 180, nullable = false)
    private String name;


    private String category;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

    @OneToOne
    private Company company;

    @OneToMany (orphanRemoval = true)
    @JoinColumn(name="customer_id")
    private Set<ImageFile> images;

}
