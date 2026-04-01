package com.brahmma.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.brahmma.dto.AuthRequest;
import com.brahmma.dto.RegisterRequest;
import com.brahmma.entity.Role;
import com.brahmma.entity.User;
import com.brahmma.repository.RoleRepository;
import com.brahmma.repository.UserRepository;
import com.brahmma.utility.JwtUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtil jwtUtil;
	

	public ResponseEntity<?> register(RegisterRequest request) {

		// Check if username exists
		if (userRepo.findByUserName(request.getUserName()) != null) {
			return ResponseEntity.badRequest().body("Username already exists");
		}

		// Check if mobile exists
		if (userRepo.findByMobile(request.getMobile()) != null) {
			return ResponseEntity.badRequest().body("Mobile number already exists");
		}

		// Fetch default ROLE_USER from DB (or create if not exists)
		Role roleUser = roleRepo.findByName("ROLE_USER");

		if (roleUser == null) {
			roleUser = new Role();
			roleUser.setName("ROLE_USER");
			roleRepo.save(roleUser);
		}

		// Create new user entity
		User user = new User();
		user.setUserName(request.getUserName());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setEmail(request.getEmail());
		user.setMobile(request.getMobile());

		// Assign default role
		Set<Role> roles = new HashSet<>();
		roles.add(roleUser);
		user.setRoles(roles);

		// Save user
		userRepo.save(user);
		
		log.info("successfull user is created");

		return ResponseEntity.ok("User registered successfully");
	}

	public ResponseEntity<?> login(AuthRequest request) {

		Authentication auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

		if (auth.isAuthenticated()) {
			String token = jwtUtil.generateToken((UserDetails) auth.getPrincipal());
			
			log.info("successfull user logged in ");
			return ResponseEntity.ok(token);
		}

		return ResponseEntity.badRequest().body("Failed to log in");
	}

}
