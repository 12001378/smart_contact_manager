package com.contactManager.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contactManager.entities.Contact;
import com.contactManager.entities.User;

public interface ContactRepo extends JpaRepository<Contact, Integer>{

	//pegination
	@Query("from Contact as c where c.user.id =:id")
	//pegable has curr-page,contact page
	public Page<Contact> findContactsByUser(@Param("id")int id, Pageable p);

	public Optional<Contact> save(Optional<Contact> contact);
	
	
	//search contact
	public List<Contact> findByNameContainingAndUser(String name,User user);

	
	
	
}
