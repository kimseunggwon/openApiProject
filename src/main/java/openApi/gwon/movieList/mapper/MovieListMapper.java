package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface MovieListMapper {

    MovieListDto findByCd(String movieCd);
    List<MovieListDto> findByNm(String movieNm);

    List<MovieListDto> findAll();

    int insertMovies(List<MovieListDto> movies);

    int countAllMovies();

    int insertDailyBoxOffice(List<DailyBoxOfficeListDto> dailyBoxOffice);

}
