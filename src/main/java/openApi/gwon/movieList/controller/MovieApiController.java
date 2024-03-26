package openApi.gwon.movieList.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.service.MovieApiCallService;
import openApi.gwon.movieList.service.MovieApiGetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MovieApiController {

    private final MovieApiGetService movieApiGetService;

    private final MovieApiCallService movieApiCallService;

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

   /* @GetMapping("/saveBoxOfficeApi")
    public List<DailyBoxOfficeListDto> saveDailyBoxOfficeApi(@RequestParam String targetDt,@RequestParam int itemsPerPage) throws JsonProcessingException{

        return movieApiGetService.saveDailyBoxOfficeApi(targetDt,itemsPerPage);
    }*/

    @GetMapping("/saveBoxOfficePeriod")
    public ResponseEntity<?> saveBoxOfficeDataForPeriod() {
        try {
            movieApiGetService.saveDailyBoxOfficeForPeriod();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/search/weeklyBoxOffice")
    public ResponseEntity<String> searchWeeklyBoxOffice (
        @RequestParam String targetDt,
        @RequestParam(required = false) String weekGb,
        @RequestParam(required = false) String repNationCd){

        try {
            return movieApiCallService.callWeeklyBoxOfficeApi(targetDt,weekGb,repNationCd);
        } catch (Exception e) {
            log.error("Exception when calling Weekly Box Office API", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while calling the API", e);
        }
    }
    @GetMapping("/insert/movieList")
    public ResponseEntity<String> testSaveMovies() throws Exception {
        try {
            movieApiGetService.testSaveMoive();
            return new ResponseEntity<>("Movies successfully saved to the database.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error saving movies to the database.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
