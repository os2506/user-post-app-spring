package com.oss.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oss.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	 // Custom method to check if a user with the given email exists
    boolean existsByEmail(String email);
    
    // Custom method to check if a user with the given username exists
    boolean existsByUsername(String username);
}