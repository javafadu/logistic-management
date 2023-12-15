package com.logistic.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_contactmessages")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)  // do not set
    private Long id;

    @Column(length = 60, nullable = false)
    private String name;


    @Column(length = 180, nullable = false)
    private String email;


    @Column(length = 60, nullable = false)
    private String subject;


    @Column(length = 200, nullable = false)
    private String body;


}
