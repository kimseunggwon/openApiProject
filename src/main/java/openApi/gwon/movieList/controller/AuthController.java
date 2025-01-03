package openApi.gwon.movieList.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.cmmn.PasswordUtils;
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

    @GetMapping("/logout.do")
    public String logout(HttpSession session) {

        // 세션 무효화
        session.invalidate();
        log.info("로그아웃 처리 완료");
        return "redirect:/login.do"; // 로그아웃 후 로그인 페이지로 리다이렉트
    }

    @GetMapping
    public String root() {
        log.info("기본 경로 접근, 로그인 페이지로 리다이렉트");
        return "redirect:/login.do";
    }


    /**
     * 로그인 get, post
     *
     * @return login
     */
    @GetMapping("/login.do")
    public String login() {

        log.info("로그인 페이지 접근");

        return "login"; // login.jsp로 이동
    }

    @PostMapping("/login.do")
    public String handleLogin(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        log.info("로그인 시도 - 사용자명 : {}", username);

        MovieUser user = movieLoginService.authenticate(username, password);

        if (user == null) {
            log.warn("로그인 실패 - 사용자명: {}", username);
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }

        // 사용자가 입력한 pw 해시화 후 검증
        String hashedPassword = PasswordUtils.hashPassword(password);
        if (!hashedPassword.equals(user.getPassword())){
            log.warn("로그인 실패 - 비밀번호 불일치 : {}",username);
            model.addAttribute("error","아이디 또는 비밀번호가 잘못되었습니다.");
            return "login";
        }

        //로그인 성공 시 세션에 사용자 정보 저장 ( 세션을 통한 사용자 인증 )
        session.setAttribute("user", user);
        log.info("로그인 성공 - 사용자명 : {}", username);

        // 리다이렉트
        return "redirect:/main/boxOfficeList.do";
    }

    /**
     * 회원가입 get, post
     *
     * @return register
     */
    @GetMapping("/register.do")
    public String register() {
        return "register"; // register.jsp로 이동
    }

    @PostMapping("/register.do")
    public String handleRegister(@RequestParam String username, @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 @RequestParam String name, @RequestParam String email,
                                 @RequestParam String birthdate, @RequestParam String phone, Model model) {


        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "register"; // 비밀번호가 일치하지 않으면 다시 회원가입 페이지로 이동
        }

        if (password.length() < 6 || !password.matches(".*\\d.*") || !password.matches(".*[A-Za-z].*")) {
            model.addAttribute("error", "비밀번호는 최소 6자리이며, 숫자와 문자가 포함되어야 합니다.");
            return "register"; // 비밀번호 조건이 맞지 않으면 다시 회원가입 페이지로 이동
        }

        // pw SHA-256으로 암호화
        String hashedPassword = PasswordUtils.hashPassword(password);
        log.info("hashedPassword = {} ", hashedPassword);

        MovieUser user = new MovieUser();
        user.setUsername(username);
        user.setPassword(hashedPassword); // 해시화된 pw
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
     * ID 찾기 처리
     */
    @PostMapping("/findId.do")
    public String handleFindId(@RequestParam String id_name,@RequestParam String id_email ,Model model) {
        log.info("ID 찾기 요청 = {}" , id_name + id_email);

        //서비스 호출하여 사용자 조회
        String username = movieLoginService.findUsernameByNameAndEmail(id_name,id_email);

        if(username == null){
            log.warn("ID 찾기 실패 - 이름 : {}, 이메일 : {}", id_name,id_email );
            model.addAttribute("idFindResult","fail");
            //model.addAttribute("error", "회원님의 정보를 찾을 수 없습니다.");
            return "login"; // 로그인 화면 이동
        }

        log.info("ID 찾기 성공 - 이름: {}, 이메일: {}, ID: {}", id_name, id_email, username);
        model.addAttribute("idFindResult","success");
        model.addAttribute("username", username);
        return "login"; // 로그인 화면 이동
    }

    /**
     * PW 찾기 처리
     */
    @PostMapping("/findPw.do")
    public String handleFindPw(@RequestParam String username, @RequestParam String email, Model model) {

        // todo 로직


        return "login";

    }

}
