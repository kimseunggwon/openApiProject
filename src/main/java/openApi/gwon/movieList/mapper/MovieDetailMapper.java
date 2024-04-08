package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.movieDetail.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface MovieDetailMapper {

    void saveMovieDetail(MovieDetailDto movieDetail);

    void saveActor(Actor actor);

    void saveCompany(Company company);
    void saveShowType(ShowType showType);
    void saveStaff(Staff staff);

    MovieDetailDto findMovieDetailByMovieCd(String movieCd);
}
