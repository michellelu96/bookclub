package com.michelle.bookclub.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.michelle.bookclub.models.User;

public interface UserRespository extends CrudRepository<User, Long>{
	
	Optional<User> findByEmail(String email);
	
}
