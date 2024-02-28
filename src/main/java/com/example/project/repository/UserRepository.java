package com.example.project.repository;

import com.example.project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이메일로 회원 정보 조회
    Optional<UserEntity> findByUserId(String UserId);
}
