package com.example.project.user.entity;

import com.example.project.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_table")
public class UserEntity {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(unique = true) // unique 제약조건 추가
    private String userId;
    @Column(nullable = false)
    private String userPassword;
    @Column
    private String userName;

    public static UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserName(userDTO.getUserName());

        return userEntity;
    }

    public static UserEntity toUpdateUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setUserPassword(userDTO.getUserPassword());
        userEntity.setUserName(userDTO.getUserName());

        return userEntity;
    }

}
