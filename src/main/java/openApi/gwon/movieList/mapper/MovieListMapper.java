package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.dailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.movieList.MovieListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Mapper
@Component
public interface MovieListMapper {

    MovieListDto findByCd(String movieCd);
    List<MovieListDto> findByNm(String movieNm);

    List<MovieListDto> findAll();

    int insertMovies(List<MovieListDto> movies);

    int countAllMovies();

    int insertDailyBoxOffice(List<DailyBoxOfficeListDto> dailyBoxOffice);

    List<MovieListDto> searchMovieList(@Param("params") Map<String,Object> params);

    int countMovies(@Param("params") Map<String, Object> params);

    List<String> findAllMovieCodes();


}
