package openApi.gwon.movieList.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import openApi.gwon.movieList.repository.MovieLoginImplRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieLoginService {

    private final MovieLoginImplRepository movieLoginImplRepository;
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

    public MovieUser findByUsername(String username) {
        log.info("MovieLoginService: 사용자 검색 - {}", username);
        return movieLoginImplRepository.findByUsername(username);
    }
}
