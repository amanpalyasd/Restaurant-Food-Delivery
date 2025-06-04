package com.util.rfd.Service;

import com.util.rfd.CustomException.EmailCannotBeUpdated;
import com.util.rfd.CustomException.PhoneNumberAlreadyRegisteredException;
import com.util.rfd.CustomException.UserNotFoundException;
import com.util.rfd.Dto.RegisterRequestDTO;
import com.util.rfd.Dto.RegisterResponseDTO;
import com.util.rfd.Entity.User;
import com.util.rfd.CustomException.EmailAlreadyRegisteredException;
import com.util.rfd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInter {

    @Autowired
    private UserRepository userRepository;

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

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDateOfBirth(request.getDateOfBirth());

        userRepository.save(user);

    }

    public List<RegisterResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(RegisterResponseDTO::new)
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
    public RegisterResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToDTO(user);
    }

    @Override
    public RegisterResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return mapToDTO(user);
    }

     private RegisterResponseDTO mapToDTO(User user) {
        return new RegisterResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhoneNumber());
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }
}
