package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.movieDetail.MovieDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface MovieDetailMapper {

    void saveMovieDetail(MovieDetail movieDetail);
}
