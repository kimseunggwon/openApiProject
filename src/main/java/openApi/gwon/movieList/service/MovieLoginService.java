package openApi.gwon.movieList.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.cmmn.PasswordUtils;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.repository.MovieLoginImplRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieLoginService {

    private final MovieLoginImplRepository movieLoginImplRepository;


    /**
     * 회원가입
     */
    public void save(MovieUser user) {
        try {
            log.info("MovieLoginService: 회원 가입 시도");
            movieLoginImplRepository.saveMovieUser(user);
            log.info("MovieLoginService: 회원 가입 성공");
        } catch (Exception e) {
            log.error("MovieLoginService: 회원 가입 실패", e);
            throw new RuntimeException("회원가입 실패: " + e.getMessage());
        }
    }

    /**
     * 회원가입시 아이디 중복 유효성 검사
     */
    public boolean isUsernameAvailable(String username) {
        MovieUser existingUser = movieLoginImplRepository.findByUsername(username);
        return existingUser == null;
    }

    /**
     * 로그인
     */
    public MovieUser authenticate(String username, String password) {
        log.info("MovieLoginService : 로그인 시도 - {}", username);

        // 데이터베이스에서 사용자 정보 가져오기
        MovieUser user = movieLoginImplRepository.findByUsername(username);

        if (user != null) {
            // 입력받은 pw를 해시화하여 비교
            String hashedPassword = PasswordUtils.hashPassword(password);
            if (hashedPassword.equals(user.getPassword())) {
                log.info("MovieLoginService: 로그인 성공 - {}", username);
                return user;
            }
        }
        log.info("MovieLoginService : 로그인 실패 - {}",username);
        return null;
    }

    /**
     *  ID 찾기 로직
     */
    public String findUsernameByNameAndEmail(String name,String email) {

        Map<String,Object> params = new HashMap<>();
        params.put("name",name);
        params.put("email",email);

        // db에서 사용자 조회
        MovieUser user = movieLoginImplRepository.findByNameAndEmail(params);

        if (user != null) {
            log.info("MovieLoginService: ID 찾기 성공 - ID: {}", user.getUsername());
            return user.getUsername();
        }

        log.info("MovieLoginService: ID 찾기 실패 - 이름: {}, 이메일: {}", name, email);
        return null;
    }

    /** PW 찾기
     *  ID 와 이메일 존재 여부 확인
     */
    public boolean verifyUserByIdAndEmail(String username,String email) {
        Map<String,Object> params = new HashMap<>();
        params.put("username",username);
        params.put("email",email);

        MovieUser user = movieLoginImplRepository.findByUsernameOrEmail(params);
        log.info("ID 와 이메일 존재 여부 user = {} ", user);
        return user != null;
    }

    /** PW 찾기
     *  비밀번호 업데이트
     */
    public boolean updatePassword(String username,String newPassword) {
        String hashedPassword = PasswordUtils.hashPassword(newPassword); // 비밀번호 해싱
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", hashedPassword);

        return movieLoginImplRepository.updatePassword(params) > 0;
    }




}
