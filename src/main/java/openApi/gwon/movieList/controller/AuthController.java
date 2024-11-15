package openApi.gwon.movieList.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import openApi.gwon.movieList.service.MovieLoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {

   private final MovieLoginService movieLoginService;

    @GetMapping("/login.do")
    public String login() {
        return "login"; // login.jsp로 이동
    }

    @GetMapping("/register.do")
    public String register() {
        return "register"; // register.jsp로 이동
    }

    @PostMapping("/register.do")
    public String handleRegister(@RequestParam String username, @RequestParam String password,
                                 @RequestParam String name, @RequestParam String email,
                                 @RequestParam String birthdate, @RequestParam String phone, Model model) {
      /*  if (movieLoginRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }*/

        MovieUser user = new MovieUser();
        user.setUsername(username);
        //user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setBirthdate(birthdate);
        user.setPhone(phone);
        user.setEnabled(true);
        log.info("user.toString() = {}" , user.toString());
        movieLoginService.save(user);
        log.info("user 저장 = {}" , user);

        return "redirect:/login.do"; // 회원가입 완료 후 로그인 페이지로 리다이렉트
    }

    @PostMapping("/login.do")
    public String handleLogin() {
        // 로그인 처리 로직
        return "redirect:/main/movieList.do"; // 로그인 성공 후 메인 페이지로 리다이렉트
    }

}
