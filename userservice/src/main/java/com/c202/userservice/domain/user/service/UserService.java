package com.c202.userservice.domain.user.service;

import com.c202.userservice.domain.user.entity.User;
import com.c202.userservice.domain.user.model.request.LoginRequestDto;
import com.c202.userservice.domain.user.model.request.SignupRequestDto;
import com.c202.userservice.domain.user.model.response.UserResponseDto;
import com.c202.userservice.domain.user.repository.UserRepository;
import com.c202.userservice.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public UserResponseDto signUp(SignupRequestDto request) {

        // 중복 검사
//        if (userRepository.existsByUsername(request.getUsername())) {}

//        if (userRepository.existsByNickname(request.getUsername())) {}

        User user = User.builder()
                .username(request.getUsername())
                // 나중에
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

    public String login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        return jwtTokenProvider.createToken(request.getUsername());
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}
