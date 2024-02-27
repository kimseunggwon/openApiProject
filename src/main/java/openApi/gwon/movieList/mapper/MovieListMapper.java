package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.MovieListDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface MovieListMapper {

    MovieListDto findByCd(String movieCd);

}
