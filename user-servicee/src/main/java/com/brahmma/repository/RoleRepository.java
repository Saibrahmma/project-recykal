package com.brahmma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.brahmma.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

	Role findByName(String string);
}
