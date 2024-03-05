package com.example.project.user.service;

import com.example.project.user.dto.MemberDTO;
import com.example.project.user.entity.MemberEntity;
import com.example.project.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    // 생성자 주입
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        // DTO -> entity
        MemberEntity memberEntity = MemberEntity.toUserEntity(memberDTO);

        // repository의 save메서드 호출
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(MemberDTO memberDTO) {
        // 이메일을 DB에서 조회
        Optional<MemberEntity> byUserId = memberRepository.findByUserId(memberDTO.getUserId());

        // 조회한 비밀번호가 사용자가 입력한 값과 일치하는지 확인
        if (byUserId.isPresent()) {
            // 조회 결과가 있을 때
            MemberEntity memberEntity = byUserId.get();
            if (memberEntity.getUserPassword().equals(memberDTO.getUserPassword())) {
                // 비밀번호 일치
                // entity -> DTO
                return MemberDTO.toUserDTO(memberEntity);
            } else {
                // 비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과가 없을 때
            return null;
        }
    }

    // member 조회
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> userDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            userDTOList.add(MemberDTO.toUserDTO(memberEntity));
        }

        return userDTOList;
    }

    // member 상세조회
    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalUserEntity = memberRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            return MemberDTO.toUserDTO((optionalUserEntity.get()));
        } else {
            return null;
        }
    }

    // update
    public MemberDTO updateForm(String myUserId) {
        Optional<MemberEntity> optionalUserEntity = memberRepository.findByUserId(myUserId);
        if (optionalUserEntity.isPresent()) {
            return MemberDTO.toUserDTO(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateUserEntity(memberDTO));
    }

    // delete
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    // userId-check
    public String userIdCheck(String userId) {
        Optional<MemberEntity> optionalUserEntity = memberRepository.findByUserId(userId);
        if (optionalUserEntity.isPresent()) {
            // 조회결과가 있으면 사용할 수 없도록
            return null;
        } else {
            return "ok";
        }
    }
}
