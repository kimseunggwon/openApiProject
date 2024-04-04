package openApi.gwon.movieList.repository;


import lombok.RequiredArgsConstructor;
import openApi.gwon.movieList.dto.movieDetail.MovieDetail;
import openApi.gwon.movieList.mapper.MovieDetailMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieDetailImplRepository implements MovieDetailRepository{

    private final MovieDetailMapper movieDetailMapper;
    @Override
    public void saveMovieDetail(MovieDetail movieDetail) {
        movieDetailMapper.saveMovieDetail(movieDetail);
    }
}
