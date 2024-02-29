package openApi.gwon.movieList.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.OpenApiConstants;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeResponse;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListResponse;
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

    public MovieListDto findByCd(String movieCd) {
        return movieListImplRepository.findByCd(movieCd);
    }

    public MovieListResponse callMovieApi(String movieNm){

        String url = OpenApiConstants.API_URL_MOVIE_LIST + "?key=" + OpenApiConstants.API_KEY_MOVIE_LIST + "&movieNm=" + movieNm;
/*        url = encodeValue(url);*/
        log.info("MOVIE_LIST url = " + url);

        ResponseEntity<MovieListResponse> response = restTemplate.getForEntity(url, MovieListResponse.class);
        log.info("reponse =  " + response);


        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            MovieListResponse MovieListResponse = response.getBody();
            log.info("Total count: {}", MovieListResponse.getMovieListResult().getTotCnt());
            log.info("Source: {}", MovieListResponse.getMovieListResult().getSource());
            log.info("Movie List: {}", MovieListResponse.getMovieListResult().getMovieList());
            return MovieListResponse;
        } else {
            log.error("Error calling MovieListResponse API: {}", response.getStatusCode());
            return null;
        }
    }

    public DailyBoxOfficeResponse callDailyBoxOfficeApi(String targetDt) {

        String url = OpenApiConstants.API_URL_DAILY_BOX_OFFICE + "?key=" + OpenApiConstants.API_KEY_DAILY_BOX_OFFICE + "&targetDt=" + targetDt;
        log.info("DAILY_BOX_OFFICE url  = " + url);



        ResponseEntity<DailyBoxOfficeResponse> responseDailyBoxOffice = restTemplate.getForEntity(url, DailyBoxOfficeResponse.class);
        log.info("responseDailyBoxOffice = " + responseDailyBoxOffice);

        if (responseDailyBoxOffice.getStatusCode() == HttpStatus.OK && responseDailyBoxOffice.getBody() != null) {
            DailyBoxOfficeResponse dailyBoxOfficeResponse = responseDailyBoxOffice.getBody();
            log.info("BoxofficeType = {} ",dailyBoxOfficeResponse.getBoxOfficeResult().getBoxofficeType());
            log.info("DailyBoxOfficeList = {} ",dailyBoxOfficeResponse.getBoxOfficeResult().getDailyBoxOfficeList());
            log.info("ShowRange = {} ",dailyBoxOfficeResponse.getBoxOfficeResult().getShowRange());

            return dailyBoxOfficeResponse;
        } else {
            log.error("Error calling DailyBoxOfficeResponse API: {}", responseDailyBoxOffice.getStatusCode());
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
