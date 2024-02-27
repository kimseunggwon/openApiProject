package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.dto.MovieListDto;


public interface MovieListRepository {

    MovieListDto findByCd(String movieCd);

}

