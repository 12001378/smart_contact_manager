package com.contactManager.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contactManager.entities.Contact;
import com.contactManager.entities.User;
import com.contactManager.repo.ContactRepo;
import com.contactManager.repo.UserRepo;

@RestController
public class searchController {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ContactRepo contatRepo;

	// serach handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal p) {

		User user = this.userRepo.getUserByUsername(p.getName());
		List<Contact> contacts = this.contatRepo.findByNameContainingAndUser(query,user );
		
		return ResponseEntity.ok(contacts);
	}
}
