package com.brahmma.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.brahmma.entity.Role;
import com.brahmma.entity.User;
import com.brahmma.repository.RoleRepository;
import com.brahmma.repository.UserRepository;

@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Check if ADMIN role exists, if not create ADMIN role.
        Role adminRole = roleRepo.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepo.save(adminRole);
        }

        // Check if any ADMIN user exists, if not create ADMIN user
        boolean adminExists = userRepo.findAll().stream()
                .anyMatch(user ->
                        user.getRoles().stream()
                                .anyMatch(r -> r.getName().equals("ROLE_ADMIN")));

        if (!adminExists) {
            User admin = new User();
            admin.setUserName("Brahmma");
            admin.setPassword(passwordEncoder.encode("Brahmma7@"));
            admin.setEmail("brahmma@gmail.com");
            admin.setMobile("9491233174");
            admin.setRoles(Set.of(adminRole));

            userRepo.save(admin);

            System.out.println("Default ADMIN user created");
        }
    }
}
