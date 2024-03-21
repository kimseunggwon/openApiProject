package openApi.gwon.movieList.controller;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.service.MovieApiGetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
@Slf4j
public class MovieViewController {

    private MovieApiGetService movieApiGetService;

    @GetMapping("/movie.do")
    public String hello(){

        return "main";
    }


}
