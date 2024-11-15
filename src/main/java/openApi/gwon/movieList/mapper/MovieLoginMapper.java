package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.login.MovieUser;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MovieLoginMapper {

    void saveMovieUser(MovieUser user);

    MovieUser findByUsername(String username);
}
