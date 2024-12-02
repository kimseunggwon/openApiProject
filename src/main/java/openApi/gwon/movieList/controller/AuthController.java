package openApi.gwon.movieList.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.service.MovieLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final MovieLoginService movieLoginService;

    /**
     * 로그인 get, post
     */
    @GetMapping("/login.do")
    public String login() {

        return "login"; // login.jsp로 이동
    }

    @PostMapping("/login.do")
    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model) {

        MovieUser user = movieLoginService.authenticate(username, password);

        if (user == null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }

        // 리다이렉트
        return "redirect:/main/boxOfficeList.do";
    }

    /**
     * 회원가입 get, post
     */

    @GetMapping("/register.do")
    public String register() {
        return "register"; // register.jsp로 이동
    }

    @PostMapping("/register.do")
    public String handleRegister(@RequestParam String username, @RequestParam String password,
                                 @RequestParam String name, @RequestParam String email,
                                 @RequestParam String birthdate, @RequestParam String phone, Model model) {

        // todo : pw 안전하게 해시함수 BCrypt 사용하여 암호화된 형태로 저장.

        MovieUser user = new MovieUser();
        user.setUsername(username);
        //user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPhone(phone);
        user.setEnabled(true);
        log.info("user.toString() = {}", user.toString());
        movieLoginService.save(user);
        log.info("user 저장 = {}", user);

        return "redirect:/login.do"; // 회원가입 완료 후 로그인 페이지로 리다이렉트
    }


    /**
     * 아이디 중복 확인
     */
    @GetMapping("/checkUsername.do")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String username) {
        boolean isAvailable = movieLoginService.isUsernameAvailable(username);
        log.info("isAvailable = {} ", isAvailable);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return response;
    }

    /**
     *  ID 찾기 처리
     */
    @PostMapping("/findId.do")
    public String handleFindId(@RequestParam String email, Model model) {

        // todo 로직

        return "login";
    }

    /**
     *  PW 찾기 처리
     */
    @PostMapping("/findPw.do")
    public String handleFindPw(@RequestParam String username, @RequestParam String email, Model model) {

        // todo 로직


        return "login";

    }

}
