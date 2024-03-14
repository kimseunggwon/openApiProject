package openApi.gwon.movieList.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.service.MovieApiGetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MovieApiController {

    private final MovieApiGetService movieApiGetService;

    @GetMapping("/getMovieList/{movieCd}")
    public MovieListDto getMovie(@PathVariable String movieCd) {

        return movieApiGetService.findByCd(movieCd);
    }

    @GetMapping("/search/movieList")
    public List<MovieListDto> searchMovieByName(@RequestParam String movieNm) throws Exception {

        return movieApiGetService.callMovieApi(movieNm);
    }

    @GetMapping("/search/dailyBoxOffice")
    public List<DailyBoxOfficeListDto> searchDailyBoxOffice(@RequestParam String targetDt) throws JsonProcessingException {

        return movieApiGetService.callDailyBoxOfficeApi(targetDt);
    }


    @GetMapping("/saveMovieApi")
    public List<MovieListDto> saveMovieApi(@RequestParam String movieNm) throws Exception {

        return movieApiGetService.saveMovieApi(movieNm);
    }

    @GetMapping("/findAll")
    public List<MovieListDto> findAll() {

        return movieApiGetService.findAllMovies();
    }

    @GetMapping("/saveBoxOfficeApi")
    public List<DailyBoxOfficeListDto> saveDailyBoxOfficeApi(@RequestParam String targetDt) throws JsonProcessingException{

        return movieApiGetService.saveDailyBoxOfficeApi(targetDt);
    }



}
