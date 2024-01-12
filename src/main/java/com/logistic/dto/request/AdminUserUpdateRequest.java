package com.logistic.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {

    @Size(max=180)
    @NotBlank(message = "Please provide name of user")
    private String name;

    @Size(min = 5, max = 180, message = "The email '${validatedValue}' must be between {min} and {max} chars long")
    @Email(message = "Please provide a valid e-mail")
    private String email;

    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4}$",message = "Please provide valid phone number")
    @NotBlank(message = "Please provide a phone number")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =
            "yyyy-MM-dd", timezone = "Turkey")
    private LocalDate birthDate;


    @Size(min = 5, max = 180, message = "The password '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide a password")
    private String password;

    private Set<String> roles;

}
