package com.example.UsersService.service;

import com.example.UsersService.records.AuthRequestRecord;
import com.example.UsersService.records.AuthResponseRecord;
import com.example.UsersService.records.RegisterRequestRecord;
import com.example.UsersService.records.UserApiResponse;
import com.example.UsersService.records.UserUpdateRequest;

import java.util.List;

public interface UserService  {
    UserApiResponse saveUser(RegisterRequestRecord registerRequestRecord);

    List<UserApiResponse> fetchAllUsers();

    UserApiResponse fetchUserById(Long id);

    List<UserApiResponse> fetchUsersByCity(String city);

    UserApiResponse updateUser(long id, UserUpdateRequest productUpdateRequest);

    AuthResponseRecord validate(AuthRequestRecord authRequest);
}
