package openApi.gwon.movieList.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.dto.login.MovieUser;
import openApi.gwon.movieList.mapper.MovieLoginMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MovieLoginImplRepository implements MovieLoginRepository{

    private final MovieLoginMapper movieLoginMapper;

    @Override
    public void saveMovieUser(MovieUser user) {
        try {
            log.info("save 진입");
            movieLoginMapper.saveMovieUser(user);
        } catch (Exception e) {
            log.info("save 실패");
            e.printStackTrace();
        }
    }

    @Override
    public MovieUser findByUsername(String username) {
        return movieLoginMapper.findByUsername(username);
    }

}
