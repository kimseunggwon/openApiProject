package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.MovieListDto;

import java.util.Optional;


public interface MovieListRepository {

    MovieListDto findByCd(String movieCd);

}

