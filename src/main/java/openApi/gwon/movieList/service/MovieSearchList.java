package openApi.gwon.movieList.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.movieList.CompanyList;
import openApi.gwon.movieList.dto.movieList.Directors;
import openApi.gwon.movieList.dto.movieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieSearchList {

    private final MovieListImplRepository movieListImplRepository;
    private final ObjectMapper objectMapper;

    /**
     * 주어진 매개변수에 따라 영화 목록을 검색
     * @param : 영화 검색하기 위한 매개변수
     * @return : MovieListDto 객체
     */
    public List<MovieListDto> searchMovieList(Map<String, Object> params) {

        List<MovieListDto> movies = movieListImplRepository.searchMovieList(params);
        movies.forEach( movie -> {
            try {
                // directorsJson 파싱 ( null 이거나 비어있는 경우 설정)
                if (movie.getDirectors() != null && !movie.getDirectors().isEmpty()) {
                    Directors[] directorsArray = objectMapper.readValue(movie.getDirectors(), Directors[].class); // Directors 배열
                    // 배열말고 , TypeReference 사용하여 리스트로 직접 변환도 가능  ( 중간에 발생할 수 있는 배열과 리스트 사이의 변환을 지운다 )
                    // List<Directors> directorsList = objectMapper.readValue(movie.getDirectors(), new TypeReference<List<Directors>>(){});
                    movie.setDirectorsList(Arrays.asList(directorsArray));
                    //log.info("directors aaa={} " , directorsArray);
                } else {
                    movie.setDirectorsList(new ArrayList<>());
                }

                // companiesJson 파싱 ( null 이거나 비어있는 경우 설정)
                if (movie.getCompanys() != null && !movie.getCompanys().isEmpty()){
                    CompanyList[] companiesArray = objectMapper.readValue(movie.getCompanys(), CompanyList[].class); // Company 배열
                    movie.setCompaniesList(Arrays.asList(companiesArray));
                    //log.info("companiesArray aaa ={} " , companiesArray);
                }
            } catch (JsonProcessingException e) {
                log.error("Error parsing Json" , e);
            }
        });
        // 필요에 따라 리포지토리 호출 전 추가 로직 가능
        log.info("영화 검색 매개변수 : {}" ,params);
        //log.info("movies 결과 : {} ",  movies);
        return movies;
    }

    public int countMovies(Map<String,Object> params) {

        log.info("영화 계산 매개변수 : {}" , params);
        return movieListImplRepository.countMovies(params);
    }

}
