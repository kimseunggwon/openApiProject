package openApi.gwon.movieList.repository;


import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.dto.movieDetail.*;
import openApi.gwon.movieList.mapper.MovieDetailMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieDetailImplRepository implements MovieDetailRepository{

    private final MovieDetailMapper movieDetailMapper;
    @Override
    public void saveMovieDetail(MovieDetailDto movieDetail) {
        movieDetailMapper.saveMovieDetail(movieDetail);
    }

    @Override
    public void saveActor(Actor actor) {
        movieDetailMapper.saveActor(actor);
    }

    @Override
    public void saveCompany(Company company) {
        movieDetailMapper.saveCompany(company);
    }

    @Override
    public void saveShowType(ShowType showType) {
        movieDetailMapper.saveShowType(showType);
    }

    @Override
    public void saveStaff(Staff staff) {
        movieDetailMapper.saveStaff(staff);
    }
}
