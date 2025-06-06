// src/main/java/com/example/HottiMaze/controller/UserController.java
package com.example.HottiMaze.controller;

import com.example.HottiMaze.dto.UserRegisterDto;
import com.example.HottiMaze.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
        return "register";
    }

    @PostMapping("/sign-up")
    public String processSignUp(
            @Valid @ModelAttribute("userRegisterDto") UserRegisterDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "비밀번호가 일치하지 않습니다.");
            return "register";
        }

        try {
            userService.registerUser(dto.getUsername(), dto.getPassword());
        } catch (IllegalStateException ex) {
            bindingResult.rejectValue("username", "duplicate", ex.getMessage());
            return "register";
        }

        return "redirect:/login?registered";
    }

    @PostMapping("/api/user/checkin")
    @ResponseBody
    public ResponseEntity<String> checkIn(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        try {
            userService.doCheckIn(principal.getUsername());
            return ResponseEntity.ok("출석체크 완료: 포인트 +50, 출석 일수 +1");
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
