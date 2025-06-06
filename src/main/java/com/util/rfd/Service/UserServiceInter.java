package com.util.rfd.Service;

import com.util.rfd.Dto.RegisterRequestDTO;
import com.util.rfd.Dto.ResponseDTO;

public interface UserServiceInter {
    void updateUser(Long id, RegisterRequestDTO request);

    ResponseDTO getUserById(Long id);
    ResponseDTO getUserByEmail(String email);
}
