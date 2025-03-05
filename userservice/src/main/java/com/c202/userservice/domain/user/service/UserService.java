package com.c202.userservice.domain.user.service;

import com.c202.userservice.domain.user.entity.User;
import com.c202.userservice.domain.user.model.request.SignupRequestDto;
import com.c202.userservice.domain.user.model.response.UserResponseDto;
import com.c202.userservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public UserResponseDto signUp(SignupRequestDto request) {
//        if (userRepository.existsByUsername(signupRequestDto.getUsername())) {}

//        if (userRepository.existsByNickname(signupRequestDto.getUsername())) {}

        User user = User.builder()
                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
                .password(request.getPassword())
                .nickname(request.getNickname())
                .birthDate(request.getBirthDate())
                .birthTime(request.getBirthTime())
                .pwQuestion(request.getPwQuestion())
                .pwAnswer(request.getPwAnswer())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.toDto(savedUser);
    }
}
