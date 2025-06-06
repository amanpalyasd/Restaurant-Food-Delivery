package com.util.rfd.Repository;

import com.util.rfd.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

    public interface RoleRepository extends JpaRepository<Role, Long> {
        Optional<Role> findByRoleName(String roleName);
    }

