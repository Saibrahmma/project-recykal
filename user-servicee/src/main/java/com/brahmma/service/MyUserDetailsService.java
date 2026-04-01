package com.brahmma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.brahmma.entity.User;
import com.brahmma.repository.UserRepository;
import com.brahmma.security.UserDetailsImpl;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		User user = repo.findByUserName(userName);

		if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userName);
        }
		return new UserDetailsImpl(user);
	}
}