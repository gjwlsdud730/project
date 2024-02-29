package com.example.project.controller;

import com.example.project.dto.BoardDTO;
import com.example.project.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 글 작성
    @GetMapping("/edit")
    public String editForm() {
        return "edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute BoardDTO boardDTO) {
        System.out.println("boardDTO = " + boardDTO);
        boardService.edit(boardDTO);
        return "index";
    }

    // 글 목록
    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    // 상세 글 조회
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        // 조회수 증가
        boardService.updateHits(id);

        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        return "detail";
    }
}



