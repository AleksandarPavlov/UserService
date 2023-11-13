package com.example.UsersService.repository;

import com.example.UsersService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByCityIgnoreCase(String city);
    Optional<User> findUserByEmail(String email);
}
