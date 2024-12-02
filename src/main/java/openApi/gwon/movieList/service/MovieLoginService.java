package openApi.gwon.movieList.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.repository.MovieLoginImplRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieLoginService {

    private final MovieLoginImplRepository movieLoginImplRepository;


    /** 회원가입
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

    /** 회원가입시 아이디 중복 유효성 검사
     */
    public boolean isUsernameAvailable(String username){
        MovieUser existingUser = movieLoginImplRepository.findByUsername(username);
        return existingUser == null ;
    }

    // ID 찾기
    // todo : pw 찾기도 필요
    public MovieUser findByUsername(String username) {
        log.info("MovieLoginService: 사용자 검색 - {}", username);
        return movieLoginImplRepository.findByUsername(username);
    }

    /** 로그인
     */
    public MovieUser authenticate(String username,String password) {
        log.info("MovieLoginService : 로그인 시도 - {}" , username);
        MovieUser user = movieLoginImplRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)){
            log.info("MovieLoginService: 로그인 성공 - {}", username);
            return user;
        }

        log.info("MovieLoginService: 로그인 실패 - {}", username);
        return null;
    }
}
