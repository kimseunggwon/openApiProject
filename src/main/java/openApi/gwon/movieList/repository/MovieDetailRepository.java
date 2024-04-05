package openApi.gwon.movieList.repository;

import openApi.gwon.movieList.dto.movieDetail.*;

public interface MovieDetailRepository {


    void saveMovieDetail(MovieDetailDto movieDetail);

    void saveActor(Actor actor);

    void saveCompany(Company company);
    void saveShowType(ShowType showType);
    void saveStaff(Staff staff);
}
