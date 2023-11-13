package com.example.UsersService.controller;

import com.example.UsersService.model.User;
import com.example.UsersService.records.AuthRequestRecord;
import com.example.UsersService.records.AuthResponseRecord;
import com.example.UsersService.records.RegisterRequestRecord;
import com.example.UsersService.records.UserApiResponse;
import com.example.UsersService.records.UserUpdateRequest;
import com.example.UsersService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping(value = "/register")
    @Operation(summary = "Register a new account")
    public ResponseEntity<UserApiResponse> register(@Valid @RequestBody RegisterRequestRecord registerRequestRecord) {
        log.info("Requested to register a user account.");
        return new ResponseEntity<UserApiResponse>(userService.saveUser(registerRequestRecord), HttpStatus.CREATED);
    }
    @GetMapping
    @Operation(summary = "Fetch all users")
    public ResponseEntity<List<UserApiResponse>> getAllUsers() {
        log.info("Requested to retrieve all users.");
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }
    @GetMapping(value ="/{id}")
    @Operation(summary = "Fetch user by ID")
    public ResponseEntity<UserApiResponse> getUserById(@PathVariable Long id) {
        log.info("Requested to retrieve user by ID: {}", id);
        return new ResponseEntity<>(userService.fetchUserById(id), HttpStatus.OK);
    }
    @GetMapping(value ="/city/{city}")
    @Operation(summary = "Fetch users by city")
    public ResponseEntity<List<UserApiResponse>> getUsersByCity(@PathVariable String city) {
        log.info("Requested to retrieve users by CITY: {}", city);
        return new ResponseEntity<>(userService.fetchUsersByCity(city), HttpStatus.OK);
    }

    @PostMapping(value ="/validate")
    @Operation(summary = "Validate user")
    public ResponseEntity<AuthResponseRecord> validate(@RequestBody AuthRequestRecord authRequest) {
        return new ResponseEntity<>(userService.validate(authRequest), HttpStatus.OK);
    }
    @PatchMapping(value = "/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserApiResponse> updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
        log.info("Requested to update user with ID: {}", id);
        return new ResponseEntity<>(userService.updateUser(id,userUpdateRequest ), HttpStatus.OK);
    }
}
