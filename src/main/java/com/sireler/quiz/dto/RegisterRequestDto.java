package com.sireler.quiz.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RegisterRequestDto {

    @NotBlank
    @Size(min = 4, message = "username must be at least 4 characters")
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(min = 6, message = "password must be at least 6 characters")
    @Size(max = 255)
    private String password;

    @NotBlank
    @Email
    private String email;
}
