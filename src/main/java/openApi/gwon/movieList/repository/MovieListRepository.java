package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;

import java.util.List;


public interface MovieListRepository {

    MovieListDto findByCd(String movieCd);

    List<MovieListDto> findByNm(String movieNm);

    List<MovieListDto> findAll();

    int insertMovies(List<MovieListDto> movies);

    int countAllMovies();

    int insertDailyBoxOffice(DailyBoxOfficeListDto dailyBoxOffice);

}

