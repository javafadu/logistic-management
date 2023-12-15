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

    @Size(min=1, max=60, message = "Your name '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide your name")
    @Column(length = 60, nullable = false)
    private String name;

    @Email(message = "Provide a valid e-mail")
    @Column(length = 180, nullable = false)
    private String email;

    @Size(min=5, max=60, message = "Subject '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide subject")
    @Column(length = 60, nullable = false)
    private String subject;

    @Size(min=5, max=200, message = "Message '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message = "Please provide message")
    @Column(length = 200, nullable = false)
    private String body;


}
