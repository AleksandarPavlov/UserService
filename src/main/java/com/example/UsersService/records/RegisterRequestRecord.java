package com.example.UsersService.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record RegisterRequestRecord(@NotBlank(message = "Email cannot be blank") String email, @NotBlank(message = "Password cannot be blank") @Pattern(regexp = "^(?=.*[A-Z])(?=\\S+$).{8,30}$") String password,@NotBlank(message = "City cannot be blank") String city, @NotBlank(message = "First name cannot be blank")String firstName, @NotBlank(message = "Last name cannot be blank")String lastName, @NotNull(message = "Birth date cannot be blank")LocalDate birthDate) {
}
