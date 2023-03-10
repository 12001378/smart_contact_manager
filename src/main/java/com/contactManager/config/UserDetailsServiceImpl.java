package com.contactManager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.contactManager.entities.User;
import com.contactManager.repo.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user details
		
		User user = userRepo.getUserByUsername(username);
		
		if(user==null)
		{
			throw new UsernameNotFoundException("cannot found user");
		}
		
		CoustomUserDetails coustomUserDetails = new CoustomUserDetails(user);
		return coustomUserDetails;
	}

}
