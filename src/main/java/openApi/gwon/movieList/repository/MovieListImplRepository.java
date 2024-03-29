package openApi.gwon.movieList.repository;

import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;

import openApi.gwon.movieList.mapper.MovieListMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MovieListImplRepository implements MovieListRepository {

    private final MovieListMapper movieListMapper;

    @Override
    public MovieListDto findByCd(String movieCd) {
        return movieListMapper.findByCd(movieCd);
    }

    @Override
    public List<MovieListDto> findByNm(String movieNm) {
        return movieListMapper.findByNm(movieNm);
    }

    @Override
    public List<MovieListDto> findAll() {
        return movieListMapper.findAll();
    }

    @Override
    public int insertMovies(List<MovieListDto> movies) {
        return movieListMapper.insertMovies(movies);
    }


    @Override
    public int insertDailyBoxOffice(List<DailyBoxOfficeListDto> dailyBoxOffice) {
        return movieListMapper.insertDailyBoxOffice(dailyBoxOffice);
    }

    @Override
    public int countAllMovies() {
        return movieListMapper.countAllMovies();
    }

    @Override
    public List<MovieListDto> searchMovieList(Map<String, Object> params) {
        return movieListMapper.searchMovieList(params);
    }

    @Override
    public int countMovies(Map<String, Object> params) {
        return movieListMapper.countMovies(params);
    }
}
