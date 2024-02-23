package openApi.gwon.movieList.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieViewController {

    @RequestMapping("/main")
    public String hello(){
        return "main";
    }
}
