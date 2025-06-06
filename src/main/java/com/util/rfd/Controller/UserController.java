package com.util.rfd.Controller;

import com.util.rfd.CustomException.EmailCannotBeUpdated;
import com.util.rfd.CustomException.UserNotFoundException;
import com.util.rfd.Dto.LoginRequestDTO;
import com.util.rfd.Dto.RegisterRequestDTO;
import com.util.rfd.Dto.ResponseDTO;
import com.util.rfd.Entity.Role;
import com.util.rfd.Entity.User;
import com.util.rfd.Repository.UserRepository;
import com.util.rfd.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/verify-user")
    public ResponseEntity<ResponseDTO> verifyUser(@RequestBody LoginRequestDTO request) {
        System.out.println("Email: " + request.getEmail());
        System.out.println("Password (raw): " + request.getPassword());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println("Password (hashed): " + user.getPassword());
        System.out.println("Password match: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        ResponseDTO dto = new ResponseDTO();
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        Set<String> roleName = new HashSet<>();
        for(Role role : user.getRoles()){
            roleName.add(role.getRoleName());
        }
        dto.setRoles(roleName);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            System.out.println("IN-");
            userService.registerUser(request);
            return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ResponseDTO>> getUsers() {
        List<ResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/updateBy/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody RegisterRequestDTO request) {
        try {
            userService.updateUser(id, request);
            return ResponseEntity.ok(Map.of("message", "User Updated successfully!"));
        }catch (EmailCannotBeUpdated e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/deleteBy/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete user: " + e.getMessage());
        }
    }

    @GetMapping("/getUserBy/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        ResponseDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

   // @PreAuthorize("hasAnyRole('USER', ADMIN)")
    @GetMapping("/getUser/by-email")
    public ResponseEntity<ResponseDTO> getUserByEmail(@RequestParam String email) {
        ResponseDTO userDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDTO);
    }
}
