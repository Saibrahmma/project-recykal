package com.brahmma.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brahmma.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUserName(String username);
	
	User findByMobile(String mobile);
}
