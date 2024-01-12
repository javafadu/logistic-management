package com.logistic.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 180)
    private String name;

    @Column(length = 180, nullable = false, unique = true)
    private String email;

    @Column(length = 180, nullable = true)
    private String phone;

    @Column(nullable = true)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @Column(length = 120, nullable = false)
    private String password;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String resetPasswordCode;

    @Column(nullable = false)
    private LocalDateTime registerDate;

    @Column(length = 20)
    private String status;

    @ManyToMany
    @JoinTable(name="tbl_user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role>  roles = new HashSet<>();


}
