package com.logistic.dto.request;

import jakarta.validation.constraints.Email;
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
public class LoginRequest {

    @Size(min = 5, max = 180, message = "The email '${validatedValue}' must be between {min} and {max} chars long")
    @Email(message = "Please provide a valid e-mail")
    private String email;

    @Size(min = 5, max = 180, message = "The password '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide a password")
    private String password;

}
