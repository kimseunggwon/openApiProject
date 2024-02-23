package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.MovieListDto;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;



@Mapper
@Component
public interface MovieListMapper {

    MovieListDto findByCd(String movieCd);

}
