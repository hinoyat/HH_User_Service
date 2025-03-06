package com.c202.userservice.domain.user.controller;

import com.c202.userservice.domain.user.model.request.LoginRequestDto;
import com.c202.userservice.domain.user.model.request.SignupRequestDto;
import com.c202.userservice.domain.user.model.response.UserResponseDto;
import com.c202.userservice.domain.user.service.UserService;
import com.c202.userservice.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@RequestBody SignupRequestDto requestDto) {
        UserResponseDto user = userService.signUp(requestDto);
        return ResponseEntity.ok(ApiResponse.success(user, "회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody LoginRequestDto requestDto) {
        String token = userService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.success(
                Map.of("token", token),
                "로그인이 성공했습니다."
        ));
    }

    @GetMapping("/check/username/{username}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkUsernameAvailability(@PathVariable String username) {
        boolean isAvailable = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(ApiResponse.success(
                Map.of("available", isAvailable),
                isAvailable ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다."
        ));
    }

    @GetMapping("/check/nickname/{nickname}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkNicknameAvailability(@PathVariable String nickname) {
        boolean isAvailable = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(ApiResponse.success(
                Map.of("available", isAvailable),
                isAvailable ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다."
        ));
    }

}
