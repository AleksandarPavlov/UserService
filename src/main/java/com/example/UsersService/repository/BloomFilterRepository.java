package com.example.UsersService.repository;

import com.example.UsersService.model.BloomFilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloomFilterRepository extends JpaRepository<BloomFilterEntity,Long> {
}
