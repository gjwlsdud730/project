package com.example.project.service;

import com.example.project.dto.BoardDTO;
import com.example.project.entity.BoardEntity;
import com.example.project.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 글 작성 (DTO -> entity)
    public void edit(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toEditEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    // 글 목록 (entity -> DTO)
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;

    }

    // 조회수
    @Transactional // update, delete 쿼리 직접 사용시 처리
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 상세 글 조회
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }
}
