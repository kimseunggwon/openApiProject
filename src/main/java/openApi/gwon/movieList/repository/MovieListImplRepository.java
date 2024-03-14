package openApi.gwon.movieList.repository;

import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;

import openApi.gwon.movieList.mapper.MovieListMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public int countAllMovies() {
        return movieListMapper.countAllMovies();
    }

    @Override
    public int insertDailyBoxOffice(DailyBoxOfficeListDto dailyBoxOffice) {
        return movieListMapper.insertDailyBoxOffice(dailyBoxOffice);
    }
}
