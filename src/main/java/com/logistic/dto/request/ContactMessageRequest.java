package com.logistic.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageRequest {

    @Size(min=1, max=60, message = "Your name '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide your name")
    private String name;

    @Email(message = "Provide a valid e-mail")
    private String email;

    @Size(min=5, max=60, message = "Subject '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide subject")
    private String subject;

    @Size(min=5, max=200, message = "Message '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide message")
    private String body;


}
