package com.example.project.controller;

import com.example.project.dto.UserDTO;
import com.example.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    // 생성자 주입
    private final UserService userService;

    // 회원가입 페이지 출력 요청
    @GetMapping("/user/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/user/signup")
    public String signup(@ModelAttribute UserDTO userDTO){
        userService.save(userDTO);
        return "login";
    }

    // 로그인 페이지 출력 요청
    @GetMapping("/user/login")
    public String loginFrom() {
        return "login";
    }

    @PostMapping("/user/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session) {
        UserDTO loginResult = userService.login(userDTO);
        if (loginResult != null) {
            // login 성공
            session.setAttribute("loginId", loginResult.getUserId());
            return "main";

        } else {
            // login 실패
            return "login";
        }
    }


    // 회원 목록 페이지 출력 요청
    @GetMapping("/user/")
    public String findAll(Model model) {
        List<UserDTO> userDTOList = userService.findAll();

        // html로 가져갈 어떤 데이터가 있을 때 model 사용
        model.addAttribute("userList", userDTOList);
        return "list";
    }

    // 회원 목록 상세 조회 출력 요청
    // @PathVariable: 경로상의 값을 가져올 때 사용
    @GetMapping("/user/{id}")
    public String findById(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.findById(id);
        model.addAttribute("user", userDTO);
        return "detail";
    }

    // 회원정보 수정 출력 요청
    @GetMapping("/user/update")
    public String updateForm(HttpSession session, Model model) {
        String myUserId = (String) session.getAttribute("loginId");
        UserDTO userDTO = userService.updateForm(myUserId);
        model.addAttribute("updateUser", userDTO);
        return "update";
    }

    @PostMapping("/user/update")
    public String update(@ModelAttribute UserDTO userDTO) {
        userService.update(userDTO);
        return "redirect:/user/" + userDTO.getId();
    }

    // 회원정보 삭제 출력 요청
    @GetMapping("/user/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/user/";
    }

    // logout
    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    // userId-check
    @PostMapping("/user/userId-check")
    public @ResponseBody String emailCheck(@RequestParam String userId) {
        return userService.userIdCheck(userId);
    }

}
