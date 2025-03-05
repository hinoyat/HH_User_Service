package com.c202.userservice.domain.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false)
    private String birthDate;

    @Column(nullable = true)
    private String birthTime;

    @Column(nullable = false)
    private String pwQuestion;

    @Column(nullable = false)
    private String pwAnswer;
    
    // CreatedAt, UpdatedAt 타임 스탬프 or String 회의
}
