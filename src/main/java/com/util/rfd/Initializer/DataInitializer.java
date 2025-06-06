package com.util.rfd.Initializer;

import com.util.rfd.Entity.Role;
import com.util.rfd.Entity.User;
import com.util.rfd.Repository.RoleRepository;
import com.util.rfd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));

        if (roleRepository.findByRoleName("USER").isEmpty()) {
            roleRepository.save(new Role(null, "USER"));
        }
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPhoneNumber("9999999999");
            adminUser.setPassword("admin@123");
            Set<Role> roleName = new HashSet<>();
            roleName.add(adminRole);
            adminUser.setRoles(roleName);

            userRepository.save(adminUser);
        }
    }
}
