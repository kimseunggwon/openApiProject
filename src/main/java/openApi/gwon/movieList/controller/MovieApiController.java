package openApi.gwon.movieList.controller;


import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.MovieListDto;
import openApi.gwon.movieList.service.MovieApiGetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MovieApiController {

    private final MovieApiGetService movieApiGetService;

    @GetMapping("/getMovieList/{movieCd}")
    public MovieListDto getMovie(@PathVariable String movieCd) {

        return movieApiGetService.findByCd(movieCd);
    }



}
