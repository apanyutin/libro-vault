package com.example.service;

import com.example.dto.user.UserRegistrationRequestDto;
import com.example.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto save(UserRegistrationRequestDto requestDto);

    UserResponseDto getById(Long id);

    UserResponseDto getByEmail(String email);

    boolean isUserExists(String email);

}
