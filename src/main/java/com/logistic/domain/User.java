package com.logistic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Column(length = 120, nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime registerDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="tbl_user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role>  roles = new HashSet<>();


}
