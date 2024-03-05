package com.example.project.user.dto;

import com.example.project.user.entity.MemberEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
// DTO: 필드로 저장한 데이터를 전달
public class MemberDTO {
    private Long id;
    private String userId;
    private String userPassword;
    private String userName;

    public static MemberDTO toUserDTO(MemberEntity memberEntity) {
        MemberDTO userDTO = new MemberDTO();
        userDTO.setId(memberEntity.getId());
        userDTO.setUserId(memberEntity.getUserId());
        userDTO.setUserPassword(memberEntity.getUserPassword());
        userDTO.setUserName(memberEntity.getUserName());

        return userDTO;
    }
}
