package com.c202.user.user.service;

import com.c202.exception.types.*;
import com.c202.user.user.entity.User;
import com.c202.user.user.model.request.UpdateIntroductionDto;
import com.c202.user.user.model.request.UpdateUserRequestDto;
import com.c202.user.user.model.response.UserResponseDto;
import com.c202.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 날짜 포맷터
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");


    // 사용자 정보 조회
    @Override
    public UserResponseDto getUserInfo(Integer userSeq) {

        User user = validateUser(userSeq);

        return UserResponseDto.toDto(user);
    }

    // 사용자 정보 수정
    @Override
    @Transactional
    public UserResponseDto updateUser(Integer userSeq, UpdateUserRequestDto request) {

        User user = validateUser(userSeq);

        // 닉네임 변경 시 중복 체크
        if (request.getNickname() != null && !request.getNickname().trim().isEmpty() && !request.getNickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new ConflictException("이미 사용 중인 닉네임입니다.");
            }
            user.updateNickname(request.getNickname());
        }

        // 비밀번호 변경
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.updatePassword(passwordEncoder.encode(request.getPassword()));
        }

        // 생일 변경
        if (request.getBirthDate() != null && !request.getBirthDate().trim().isEmpty()) {
            user.updateBirthDate(request.getBirthDate());
        }

        if (request.getIconSeq() != null) {
            user.updateIconSeq(request.getIconSeq());
        }

        // 수정 시간 업데이트
        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        user.updateInfo(now);

        return UserResponseDto.toDto(user);
    }


    // 회원 탈퇴
    @Override
    @Transactional
    public void deleteUser(Integer userSeq) {
        User user = validateUser(userSeq);

        String now = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        user.updateDeletedAt(now);
        // 물리적 삭제가 아닌 논리적 삭제 처리
        user.deleteUser();
    }

    @Override
    @Transactional
    public UserResponseDto updateIntroduction(Integer userSeq, UpdateIntroductionDto request) {
        User user = validateUser(userSeq);

        user.updateIntroduction(request.getIntroduction());

        userRepository.save(user);

        return UserResponseDto.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto getRandomUser() {
        User randomUser = userRepository.findRandomActiveUser()
                .orElseThrow(() -> new NotFoundException("사용 가능한 사용자가 없습니다."));

        return UserResponseDto.toDto(randomUser);
    }

    private User validateUser(Integer userSeq) {
        User user = userRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        if ("Y".equals(user.getIsDeleted())) {
            throw new BadRequestException("이미 탈퇴한 계정입니다.");
        }

        return user;
    }

    public String getUserBirthDate(Integer userSeq){
        User user = validateUser(userSeq);
        return user.getBirthDate();
    }
}