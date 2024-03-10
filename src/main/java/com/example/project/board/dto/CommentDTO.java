package com.example.project.board.dto;

import com.example.project.board.entity.CommentEntity;
import com.example.project.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
//        commentDTO.setBoardId(commentEntity.getBoardEntity().getId()); // Service 메서드에 @Transactional
        commentDTO.setBoardId(boardId);

        // getUserEntity가 null이 아닌 경우에만 처리
        if (commentEntity.getUserEntity() != null) {
            commentDTO.setCommentWriter(commentEntity.getUserEntity().getUserName());
        } else {
            // userEntity가 null인 경우의 로직 수행
            commentDTO.setCommentWriter(null);
        }

        return commentDTO;
    }
}
