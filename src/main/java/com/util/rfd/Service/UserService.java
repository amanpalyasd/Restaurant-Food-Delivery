package com.util.rfd.Service;

import com.util.rfd.CustomException.EmailAlreadyRegisteredException;
import com.util.rfd.CustomException.EmailCannotBeUpdated;
import com.util.rfd.CustomException.PhoneNumberAlreadyRegisteredException;
import com.util.rfd.CustomException.UserNotFoundException;
import com.util.rfd.Dto.RegisterRequestDTO;
import com.util.rfd.Dto.ResponseDTO;
import com.util.rfd.Entity.Role;
import com.util.rfd.Entity.User;
import com.util.rfd.Repository.RoleRepository;
import com.util.rfd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public void registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException("Email '" + request.getEmail() + "' is already registered.");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyRegisteredException("Phone number '" + request.getPhoneNumber() + "' is already registered.");
        }

        if (!request.getPhoneNumber().matches("^[6-9]\\d{9}$")) {
            throw new IllegalArgumentException("Phone number must be a valid 10-digit Indian number and remove +91");
        }

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));

        Set<Role> roleName = new HashSet<>();
        roleName.add(userRole);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPassword(request.getPassword());
        user.setRoles(roleName);

        userRepository.save(user);

    }

    public List<ResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(ResponseDTO::new)
                .collect(Collectors.toList());
    }


    @Override
    public void updateUser(Long id, RegisterRequestDTO request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        if (request.getEmail() != null && !request.getEmail().isBlank() && !request.getEmail().equals(existingUser.getEmail())) {
            throw new EmailCannotBeUpdated("Email cannot be updated.");
        }
        if (request.getUsername() != null) existingUser.setUsername(request.getUsername());
        if (request.getPhoneNumber() != null) existingUser.setPhoneNumber(request.getPhoneNumber());
        if (request.getDateOfBirth() != null) existingUser.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(existingUser);
    }

    @Override
    public ResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToDTO(user);
    }

    @Override
    public ResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToDTO(user);
    }

     private ResponseDTO mapToDTO(User user) {
         ResponseDTO dto = new ResponseDTO();
         dto.setId(user.getId());
         dto.setEmail(user.getEmail());
         dto.setUsername(user.getUsername());
         dto.setPassword(user.getPassword());
         dto.setPhoneNumber(user.getPhoneNumber());
         Set<String> roleName = new HashSet<>();
         for(Role role : user.getRoles()){
             roleName.add(role.getRoleName());
         }
         System.out.println("ROLE :::" +roleName);
         dto.setRoles(roleName);
         return dto;
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }
}
