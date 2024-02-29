package openApi.gwon.movieList.repository;

import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;

import openApi.gwon.movieList.mapper.MovieListMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieListImplRepository implements MovieListRepository {

    private final MovieListMapper movieListMapper;

    @Override
    public MovieListDto findByCd(String movieCd) {
        return movieListMapper.findByCd(movieCd);
    }
}
