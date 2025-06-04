package com.util.rfd.Service;

import com.util.rfd.Dto.RegisterRequestDTO;
import com.util.rfd.Dto.RegisterResponseDTO;

public interface UserServiceInter {
    void updateUser(Long id, RegisterRequestDTO request);

    RegisterResponseDTO getUserById(Long id);
    RegisterResponseDTO getUserByEmail(String email);
}
