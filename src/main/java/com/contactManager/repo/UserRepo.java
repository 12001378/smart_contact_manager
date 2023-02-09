package com.contactManager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactManager.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	@Query("select u from User  u where u.email= :email")
	
	public User getUserByUsername(@Param("email") String email);
	
	
}
