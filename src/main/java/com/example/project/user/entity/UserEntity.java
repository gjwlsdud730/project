package com.example.project.user.entity;

import com.example.project.user.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "user_table")
public class MemberEntity {
    @Id // pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(unique = true) // unique 제약조건 추가
    private String userId;
    @Column(nullable = false)
    private String userPassword;
    @Column
    private String userName;

    public static MemberEntity toUserEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUserId(memberDTO.getUserId());
        memberEntity.setUserPassword(memberDTO.getUserPassword());
        memberEntity.setUserName(memberDTO.getUserName());

        return memberEntity;
    }

    public static MemberEntity toUpdateUserEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setUserId(memberDTO.getUserId());
        memberEntity.setUserPassword(memberDTO.getUserPassword());
        memberEntity.setUserName(memberDTO.getUserName());

        return memberEntity;
    }

}
