package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.dto.login.MovieUser;

public interface MovieLoginRepository {
    void saveMovieUser(MovieUser user);

    MovieUser findByUsername(String username);
}
