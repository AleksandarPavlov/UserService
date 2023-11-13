package com.example.UsersService.service;

import com.example.UsersService.exceptions.CustomException;
import com.example.UsersService.model.User;
import com.example.UsersService.records.AuthRequestRecord;
import com.example.UsersService.records.AuthResponseRecord;
import com.example.UsersService.records.RegisterRequestRecord;
import com.example.UsersService.records.UserApiResponse;
import com.example.UsersService.records.UserUpdateRequest;
import com.example.UsersService.repository.UserRepository;
import com.google.common.hash.BloomFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final BloomFilterService bloomFilterService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImplement.class);

    public UserServiceImplement(UserRepository userRepository, BloomFilterService bloomFilterService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bloomFilterService = bloomFilterService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserApiResponse saveUser(RegisterRequestRecord registerRequestRecord) {

        log.info("Attempting to save user with email: {}",registerRequestRecord.email());

        BloomFilter<String> filter = bloomFilterService.getBloomFilter();
        if (filter.mightContain(registerRequestRecord.email())) {
            log.error("Error saving user with email: {}. Account with the given email already exists",registerRequestRecord.email());
            throw new CustomException("Account with the given email already exists.", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setBirthDate(registerRequestRecord.birthDate());
        user.setCity(registerRequestRecord.city());
        user.setEmail(registerRequestRecord.email());
        user.setPassword(passwordEncoder.encode(registerRequestRecord.password()));
        user.setFirstName(registerRequestRecord.firstName());
        user.setLastName(registerRequestRecord.lastName());
        userRepository.save(user);

        filter.put(user.getEmail());
        bloomFilterService.saveBloomFilter(filter);
        return UserApiResponse.map(user);
    }

    @Override
    public List<UserApiResponse> fetchAllUsers() {
        log.info("Fetching all users.");
        List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(UserApiResponse::map)
                .collect(Collectors.toList());

    }

    @Override
    public UserApiResponse fetchUserById(Long id) {
        log.info("Attempting to retrieve user with ID: {}", id);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return UserApiResponse.map(user);
        } else {
            log.error("Error retrieving user with ID: {}", id);
            throw new CustomException("User not found with ID: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<UserApiResponse> fetchUsersByCity(String city) {
        log.info("Attempting to retrieve users in CITY: {}", city);
        return userRepository.findUsersByCityIgnoreCase(city).stream()
                .map(UserApiResponse::map)
                .collect(Collectors.toList());
    }

    @Override
    public UserApiResponse updateUser(long id, UserUpdateRequest userUpdateRequest) {
        log.info("Updating user with ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            Optional.ofNullable(userUpdateRequest.city())
                    .filter(city -> !city.isEmpty())
                    .ifPresent(user::setCity);

            Optional.ofNullable(userUpdateRequest.firstName())
                    .filter(firstName -> !firstName.isEmpty())
                    .ifPresent(user::setFirstName);

            Optional.ofNullable(userUpdateRequest.lastName())
                    .filter(lastName -> !lastName.isEmpty())
                    .ifPresent(user::setLastName);


            return UserApiResponse.map(userRepository.save(user));

        } else {
            log.info("Failed to update user with ID: {}", id);
            throw new CustomException("User with ID: " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public AuthResponseRecord validate(AuthRequestRecord authRequest) {
        Optional<User> optionalUser = userRepository.findUserByEmail(authRequest.email());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new AuthResponseRecord(user.getEmail(), user.getPassword());

        } else {
            throw new CustomException("Authentication failed", HttpStatus.FORBIDDEN);
        }
    }

}
