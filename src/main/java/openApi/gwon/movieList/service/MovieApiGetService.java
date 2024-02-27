package openApi.gwon.movieList.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.MovieListDto;
import openApi.gwon.movieList.dto.MovieListResponse;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieApiGetService {

    private final MovieListImplRepository movieListImplRepository;
    private final RestTemplate restTemplate;
    private final String apiKey ="08e4c0307e4287265f2bef14c3c73aa4";
    private final String apiUrl = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json";

    public MovieListDto findByCd(String movieCd) {
        return movieListImplRepository.findByCd(movieCd);
    }

    public MovieListResponse callMovieApi(String movieNm){
        String url = apiUrl + "?key=" + apiKey + "&movieNm=" + encodeValue(movieNm);

        ResponseEntity<MovieListResponse> response = restTemplate.getForEntity(url,MovieListResponse.class);
        log.info("reponse =  " + response);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            MovieListResponse movieListResponse = response.getBody();
            log.info("Total count: {}", movieListResponse.getMovieListResult().getTotCnt());
            log.info("Source: {}", movieListResponse.getMovieListResult().getSource());
            log.info("Movie List: {}", movieListResponse.getMovieListResult().getMovieList());
            return movieListResponse;
        } else {
            log.error("Error calling Movie API: {}", response.getStatusCode());
            return null;
        }

    }


    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding URL parameter", e);
            return "";
        }
    }

}
