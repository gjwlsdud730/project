package com.example.project.board.service;

import com.example.project.board.dto.CommentDTO;
import com.example.project.board.repository.BoardRepository;
import com.example.project.board.repository.CommentRepository;
import com.example.project.board.entity.BoardEntity;
import com.example.project.board.entity.CommentEntity;
import com.example.project.user.entity.UserEntity;
import com.example.project.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    // 댓글 작성
    @Transactional
    public Long save(CommentDTO commentDTO) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        Optional<UserEntity> optionalUserEntity = userRepository.findByUserName(commentDTO.getCommentWriter());

        if (optionalBoardEntity.isEmpty()) {
            throw new EntityNotFoundException("해당 게시글이 존재하지 않습니다.");
        }
        if (optionalUserEntity.isEmpty()) {
            // 사용자를 찾을 수 없는 경우에 대한 명확한 예외 처리
            throw new EntityNotFoundException("로그인 사용자를 찾을 수 없습니다. 세션이 만료되었거나 잘못된 사용자 이름일 수 있습니다.");
        }

        BoardEntity boardEntity = optionalBoardEntity.get();
        UserEntity userEntity = optionalUserEntity.get();
        CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity, userEntity);
        return commentRepository.save(commentEntity).getId();
    }



    @Transactional
    // 댓글 출력
    public List<CommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id=? ord by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);

        // entityList -> DTOList
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            commentDTOList.add(CommentDTO.toCommentDTO(commentEntity, boardId));
        }
        return commentDTOList;
    }
}
