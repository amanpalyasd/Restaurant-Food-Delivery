package com.util.rfd.Dto;

import com.util.rfd.Entity.Role;
import com.util.rfd.Entity.User;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
public class ResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private Set<String> roles;

    public ResponseDTO(Set<String> roles, String phoneNumber, String password, String email, Long id, String username) {
        this.roles = roles;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.email = email;
        this.id = id;
        this.username = username;
    }

    public ResponseDTO() {

    }

    public ResponseDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.roles = new HashSet<>();
        for(Role role : user.getRoles()){
            this.roles.add(role.getRoleName());
        }
    }



}
