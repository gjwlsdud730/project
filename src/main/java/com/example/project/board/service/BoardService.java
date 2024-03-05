package com.example.project.service;

import com.example.project.dto.BoardDTO;
import com.example.project.entity.BoardEntity;
import com.example.project.entity.BoardFileEntity;
import com.example.project.repository.BoardFileRepository;
import com.example.project.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // 글 작성 (DTO -> entity)
    public void edit(BoardDTO boardDTO) throws IOException {
        if (boardDTO.getBoardFile().get(0).isEmpty()) {
            // 첨부 파일 없음
            boardDTO.setFileAttached(0); // 파일이 없는 경우 fileAttached를 0으로 설정
            BoardEntity boardEntity = BoardEntity.toEditEntity(boardDTO);
            boardRepository.save(boardEntity);
        } else {
            // 첨부 파일이 있는 경우
            BoardEntity boardEntity = BoardEntity.toEditFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).orElseThrow(() -> new RuntimeException("Board not found"));

            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                if (!boardFile.isEmpty()) { // 파일이 비어있지 않은 경우에만 처리
                    String originalFilename = boardFile.getOriginalFilename();
                    String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

                    // 맥 로컬
                    String savePath = "/Users/jinyoung/springboot_img/" + storedFileName;

                    // 리눅스 서버
//                     String savePath = "/home/gjwlsdud730/springboot_img/" + storedFileName;

                    boardFile.transferTo(new File(savePath));

                    BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                    boardFileRepository.save(boardFileEntity);
                }
            }
        }

    }


    // 글 목록
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        // entityList -> DTOList
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
    @Transactional
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

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);

        return findById(boardDTO.getId());
    }

    // 글 삭제
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 페이징
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 5개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        // 목록: id, 작성자, 제목, 조회, 날짜
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(
            board.getId(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()
        ));

        return boardDTOS;
    }
}
