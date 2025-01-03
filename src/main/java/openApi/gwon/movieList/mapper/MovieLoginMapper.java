package openApi.gwon.movieList.mapper;

import openApi.gwon.movieList.dto.login.MovieUser;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;

import java.util.Map;

@Mapper
@Component
public interface MovieLoginMapper {

    void saveMovieUser(MovieUser user);

    MovieUser findByUsername(String username);

    MovieUser findByNameAndEmail(Map<String,Object> params);

    // todo 질문 : 기존에 구현되어잇는 쿼리에 추가를 해서 findByNameAndEmail 대신에 구현을 하면 안되나?


}
