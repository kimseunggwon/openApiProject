package openApi.gwon.movieList.controller;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.service.MovieApiGetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
@Slf4j
public class MovieViewController {

    private MovieApiGetService movieApiGetService;

    @GetMapping("/boxOfficeList.do")
    public String boxOfficeList(){

        return "boxOfficeList";
    }

    @GetMapping("/movieList.do")
    public String movieList() {

        return "movieList";
    }

    @GetMapping("/new/movieList/Detail.do")
    public String movieListDetail(@RequestParam String movieCd, Model model) {

        model.addAttribute("movieCd",movieCd);

        return "movieListDetail";
    }

    @GetMapping("/new/company/info.do")
    public String companyInfoDetail(@RequestParam String companyCd, @RequestParam String movieListId ,Model model) {

        model.addAttribute("companyCd" , companyCd);
        model.addAttribute("movieListId" , movieListId);
        log.info("companyInfoDetail companyCd = {}", companyCd);
        log.info("companyInfoDetail movieListId = {}" , movieListId);

        return "companyInfoDetail";
    }


}
