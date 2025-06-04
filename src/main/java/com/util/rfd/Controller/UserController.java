package com.util.rfd.Controller;

import com.util.rfd.CustomException.EmailCannotBeUpdated;
import com.util.rfd.CustomException.UserNotFoundException;
import com.util.rfd.Dto.RegisterRequestDTO;
import com.util.rfd.Dto.RegisterResponseDTO;
import com.util.rfd.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
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
    public ResponseEntity<List<RegisterResponseDTO>> getUsers() {
        List<RegisterResponseDTO> users = userService.getAllUsers();
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
    public ResponseEntity<RegisterResponseDTO> getUserById(@PathVariable Long id) {
        RegisterResponseDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/getUser/by-email")
    public ResponseEntity<RegisterResponseDTO> getUserByEmail(@RequestParam String email) {
        RegisterResponseDTO userDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDTO);
    }
}
