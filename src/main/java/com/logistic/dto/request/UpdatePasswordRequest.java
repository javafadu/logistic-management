package com.logistic.dto.request;

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
public class UpdatePasswordRequest {

    @NotBlank(message = "Please provide your current password")
    private String oldPassword;

    @Size(min = 5, max = 180, message = "The password '${validatedValue}' must be between {min} and {max} chars long")
    @NotBlank(message = "Please provide a new password")
    private String newPassword;

}
