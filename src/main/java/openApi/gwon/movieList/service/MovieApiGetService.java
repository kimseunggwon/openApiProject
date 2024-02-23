package openApi.gwon.movieList.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieApiGetService {

    private final MovieListImplRepository movieListImplRepository;

    public MovieListDto findByCd(String movieCd) {
        return movieListImplRepository.findByCd(movieCd);
    }



}
