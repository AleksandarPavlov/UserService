package com.example.UsersService.records;

import com.example.UsersService.model.User;

import java.time.LocalDate;


public record UserApiResponse(String email, String firstName, String lastName, String city, LocalDate birthDate) {

    public static UserApiResponse map(User user) {
        return new UserApiResponse(user.getEmail(), user.getFirstName(), user.getLastName(), user.getCity(), user.getBirthDate());
    }
}
