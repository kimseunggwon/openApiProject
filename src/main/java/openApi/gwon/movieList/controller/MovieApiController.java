package openApi.gwon.movieList.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.service.MovieApiCallService;
import openApi.gwon.movieList.service.MovieApiGetService;
import openApi.gwon.movieList.service.MovieSearchList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MovieApiController {

    private final MovieApiGetService movieApiGetService;

    private final MovieApiCallService movieApiCallService;

    private final MovieSearchList movieSearchList;

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

    /**
     *
     */
    @GetMapping("/search/movie")
    public ResponseEntity<Map<String,Object>> searchMovies(
            @RequestParam(required = false) String movieName,
            @RequestParam(required = false) String directorName,
            @RequestParam(required = false) String productionYear,
            @RequestParam(required = false) String openDateStart,
            @RequestParam(required = false) String openDateEnd,
            @RequestParam(defaultValue = "1") int page, // 현재 페이지 번호
            @RequestParam(defaultValue = "10") int size) { // 페이지당 보여줄 항목 수

        int offset = (page - 1) * size; // offset 계산 ( 건너뛸 항목의 수 , 어디서부터 데이터를 가져올지 결정 )
        log.info("offset = {}" ,offset);

        Map<String, Object> params = new HashMap<>();
        params.put("movieNm", movieName);
        params.put("directorName", directorName);
        params.put("prdtYear", productionYear);
        params.put("openDtStart", openDateStart);
        params.put("openDtEnd", openDateEnd);
        params.put("offset", offset);
        params.put("limit", size);
        log.info("params = {} " , params);

        List<MovieListDto> movies = movieSearchList.searchMovieList(params);
        int totalMovies = movieSearchList.countMovies(params);

        Map<String,Object> response = new HashMap<>();
        response.put("movies",movies);
        response.put("totalMovies", totalMovies);

        return ResponseEntity.ok(response);
    }




}
