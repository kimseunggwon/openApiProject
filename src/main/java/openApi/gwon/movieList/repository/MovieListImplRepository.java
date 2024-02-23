package openApi.gwon.movieList.repository;

import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.MovieListDto;

import openApi.gwon.movieList.mapper.MovieListMapper;
import openApi.gwon.movieList.repository.MovieListRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovieListImplRepository implements MovieListRepository {

    private final MovieListMapper movieListMapper;

    @Override
    public MovieListDto findByCd(String movieCd) {
        return movieListMapper.findByCd(movieCd);
    }
}
