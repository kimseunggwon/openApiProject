package openApi.gwon.movieList.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.cmmn.PasswordUtils;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.service.MailgunService;
import openApi.gwon.movieList.service.MovieLoginService;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Bool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final MovieLoginService movieLoginService;

    private final MailgunService mailgunService;

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

        // todo : 로그인 , PW 5회 실패시 -> 잠김처리 or pw 틀린때마다 alert창 => 총 1회 틀렸고 5회 틀릴시 아이디 잠금처리 됩니다.
        // todo : 로그인 보통 인증 or 실패시 처리 어떻게 하는지 알아보기


        // 사용자가 입력한 pw 해시화 후 검증
        String hashedPassword = PasswordUtils.hashPassword(password);
        if (!hashedPassword.equals(user.getPassword())) {
            log.warn("로그인 실패 - 비밀번호 불일치 : {}", username);
            model.addAttribute("error", "아이디 또는 비밀번호가 잘못되었습니다.");
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
    public String handleFindId(@RequestParam String id_name, @RequestParam String id_email, Model model) {
        log.info("ID 찾기 요청 = {}", id_name + id_email);

        //서비스 호출하여 사용자 조회
        String username = movieLoginService.findUsernameByNameAndEmail(id_name, id_email);

        if (username == null) {
            log.warn("ID 찾기 실패 - 이름 : {}, 이메일 : {}", id_name, id_email);
            model.addAttribute("idFindResult", "fail");
            //model.addAttribute("error", "회원님의 정보를 찾을 수 없습니다.");
            return "login"; // 로그인 화면 이동
        }

        log.info("ID 찾기 성공 - 이름: {}, 이메일: {}, ID: {}", id_name, id_email, username);
        model.addAttribute("idFindResult", "success");
        model.addAttribute("username", username);
        return "login"; // 로그인 화면 이동
    }


    /**
     * pw 찾기
     */
    @PostMapping("/findPw.do")
    @ResponseBody
    public Map<String, Object> handleFindPw(@RequestParam String pw_id, @RequestParam String pw_email, HttpSession session) {
        log.info("PW 찾기 요청 - 아이디: {}, 이메일: {}", pw_id, pw_email);

        Map<String, Object> response = new HashMap<>();

        // 1. 사용자 ID , 이메일 존재 확인
        if (!movieLoginService.verifyUserByIdAndEmail(pw_id, pw_email)) {
            response.put("success", false);
            response.put("message", "아이디와 이메일을 확인해주세요.");// todo : JSP 전달 alert 창 화면에서 -> message", "아이디와 이메일을 확인해주세요."
            return response;
        }

        // 2. 인증 코드 생성 및 이메일 인증 전송
        String code = generateVerificationCode(); // 간단한 랜덤 코드 생성
        log.info("pw_email 확인 = {}" , pw_email);
        boolean emailSent = mailgunService.sendEmail(pw_email, "비밀번호 찾기 인증 코드", "인증 코드: " + code);
        log.info("emailSent 확인 =  {}",emailSent);

        if (!emailSent) {
            response.put("success", false);
            response.put("message", "이메일 전송에 실패했습니다."); // todo : JSP 전달 alert 창 화면에서 -> "이메일 전송에 실패했습니다."
            return response;
        }

        //인증 코드 저장 (세션 또는 Redis)
        session.setAttribute("verificationCode", code);
        session.setAttribute("email", pw_email);     //세션에 왜 굳이 사용자 id랑 pw를 저장?
        session.setAttribute("username", pw_id);

        response.put("success", true);
        response.put("step", "handleFindPwSuccess");
        return response;
    }

    /**
     * 이메일 인증 확인 ->
     */
    @PostMapping("/verifyCode")
    @ResponseBody
    public Map<String, Object> verifyCode(@RequestParam String verificationCode, @RequestParam String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String savedCode = (String) session.getAttribute("verificationCode");
        String savedEmail = (String) session.getAttribute("email");
        log.info("savedCode 확인 = {}",savedCode);

        if (savedCode == null || !savedCode.equals(verificationCode)) {
            response.put("success", false);
            response.put("message", "인증 코드가 일치하지 않습니다.");// todo : JSP 전달 alert 창 화면에서 -> "인증 코드가 일치하지 않습니다"
            return response;
        }

        log.info("사용자 email 확인 = {}", email);
        if (!email.equals(savedEmail)) {
            response.put("success", false);
            response.put("message", "이메일이 일치하지 않습니다.");
            return response;
        }

        // 인증 완료
        session.setAttribute("verified", true);
        response.put("success", true);
        response.put("step", "verifyCodeSuccess");
        return response;
    }

    /**
     * PW 찾기 인증 성공 -> PW 변경만
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public Map<String, Object> resetPassword(@RequestParam String newPassword, HttpSession session) {
        log.info("PW 변경 요청");

        Map<String, Object> response = new HashMap<>();
        Boolean isVerified = (boolean) session.getAttribute("verified");
        String username = (String) session.getAttribute("username");
        log.info("isVerified 확인 = {}" , isVerified);

        // 1. 인증 여부 확인
        if (isVerified == null || !isVerified) {
            response.put("success", false);
            response.put("message", "인증이 필요합니다."); // 화면에 alert 표출
            return response;
        }
        // 비밀번호 검증
        if (!isValidPassword(newPassword)) {
            response.put("success", false);
            response.put("message", "비밀번호는 최소 6자리이며, 숫자와 문자가 포함되어야 합니다."); // 화면에 alert 표출
            return response;
        }

        // 3. 비밀번호 변경
        boolean isUpdated = movieLoginService.updatePassword(username, newPassword);
        log.info("isUpdated 확인 = {} ",isUpdated);

        if (isUpdated) {
            response.put("success", true);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            session.invalidate(); // 인증 정보 삭제
        } else {
            log.warn("PW 변경 실패 - 사용자 : {}", username);
            response.put("success", false);
            response.put("message", "비밀번호 변경에 실패했습니다.");
        }

        return response;
    }

    // Email 인증 번호 생성
    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // 6자리 랜덤 숫자
    }

    // PW 검증 로직
    private boolean isValidPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Za-z].*");
    }


}