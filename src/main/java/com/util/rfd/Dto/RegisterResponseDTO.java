package com.util.rfd.Dto;

import com.util.rfd.Entity.User;
import lombok.*;

@Data
public class RegisterResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber;

    public RegisterResponseDTO(Long id, String phoneNumber, String email, String username) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
    }

    public RegisterResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
    }



}
