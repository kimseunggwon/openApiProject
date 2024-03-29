package openApi.gwon.movieList.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieSearchList {

    private final MovieListImplRepository movieListImplRepository;

    /**
     * 주어진 매개변수에 따라 영화 목록을 검색
     * @param : 영화 검색하기 위한 매개변수
     * @return : MovieListDto 객체
     */
    public List<MovieListDto> searchMovieList(Map<String, Object> params) {

        // 필요에 따라 리포지토리 호출 전 추가 로직 가능
        log.info("영화 검색 매개변수 : {}" ,params);
        return movieListImplRepository.searchMovieList(params);
    }

    public int countMovies(Map<String,Object> params) {

        log.info("영화 계산 매개변수 : {}" , params);
        return movieListImplRepository.countMovies(params);
    }

}
