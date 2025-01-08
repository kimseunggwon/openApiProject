package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.dto.login.MovieUser;

import java.util.Map;

public interface MovieLoginRepository {
    void saveMovieUser(MovieUser user);

    MovieUser findByUsername(String username);

    MovieUser findByNameAndEmail(Map<String,Object> params);

    int updatePassword(Map<String,Object> params);

    MovieUser findByUsernameOrEmail(Map<String,Object> params);
}
