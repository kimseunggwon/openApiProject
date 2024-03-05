package openApi.gwon.movieList.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.OpenApiConstants;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeResponse;
import openApi.gwon.movieList.dto.MovieList.Directors;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieApiGetService {

    private final MovieListImplRepository movieListImplRepository;
    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public MovieListDto findByCd(String movieCd) {
        return movieListImplRepository.findByCd(movieCd);
    }

    /**
     * 영화목록 조회 API 서비스
     * @param movieNm
     * @return MovieListDto
     * @throws Exception
     */
    public List<MovieListDto> callMovieApi(String movieNm) throws Exception{

        //String url = OpenApiConstants.API_URL_MOVIE_LIST + "?key=" + OpenApiConstants.API_KEY_MOVIE_LIST + "&movieNm=" + movieNm;
        //log.info("MOVIE_LIST url = " + url);

        String url = UriComponentsBuilder.fromHttpUrl(OpenApiConstants.API_URL_MOVIE_LIST)
                .queryParam("key", OpenApiConstants.API_KEY_LIST)
                .queryParam("&movieNm", movieNm)
                .toUriString();
        log.info("MOVIE_LIST url = " + url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("reponse =  " + response);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            String responseBody = response.getBody();

                HashMap<String, Object> resultMap = objectMapper.readValue(responseBody,HashMap.class);
                HashMap<String, Object> movieListResult = (HashMap<String, Object>) resultMap.get("movieListResult");
                List<HashMap<String, Object>> movieList = (List<HashMap<String, Object>>) movieListResult.get("movieList");

                List<MovieListDto> movieListDtos = new ArrayList<>();
                for (HashMap<String,Object> movie : movieList) {
                    MovieListDto movieDto = new MovieListDto();
                    movieDto.setMovieListId((UUID.randomUUID().toString()));
                    movieDto.setMovieCd((String) movie.get("movieCd"));
                    movieDto.setMovieNm((String) movie.get("movieNm"));
                    movieDto.setMovieNmEn((String) movie.get("movieNmEn"));
                    movieDto.setPrdtYear((String) movie.get("prdtYear"));
                    movieDto.setOpenDt((String) movie.get("openDt"));
                    movieDto.setTypeNm((String) movie.get("typeNm"));
                    movieDto.setPrdtStatNm((String) movie.get("prdtStatNm"));
                    movieDto.setNationAlt((String) movie.get("nationAlt"));
                    movieDto.setGenreAlt((String) movie.get("genreAlt"));
                    movieDto.setRepNationNm((String) movie.get("repNationNm"));
                    movieDto.setRepGenreNm((String) movie.get("repGenreNm"));

                    List<HashMap<String,Object>> directors = (List<HashMap<String, Object>>) movie.getOrDefault("directors",new ArrayList<>());
                    List<Directors> directorsList = new ArrayList<>();
                    for (HashMap<String, Object> director : directors) {
                        Directors directorsDto = new Directors();
                        directorsDto.setPeopleNm((String) director.get("peopleNm"));
                        directorsList.add(directorsDto);
                    }
                    movieDto.setDirectorsList(directorsList);



                    movieListDtos.add(movieDto);
                }
                return movieListDtos;

        }  else {
            throw new RuntimeException("Failed to call Movie API: " + response.getStatusCode());
        }
    }

    public DailyBoxOfficeResponse callDailyBoxOfficeApi(String targetDt) {

        String url = OpenApiConstants.API_URL_DAILY_BOX_OFFICE + "?key=" + OpenApiConstants.API_KEY_LIST + "&targetDt=" + targetDt;
        log.info("DAILY_BOX_OFFICE url  = " + url);



        ResponseEntity<DailyBoxOfficeResponse> responseDailyBoxOffice = restTemplate.getForEntity(url, DailyBoxOfficeResponse.class);
        log.info("responseDailyBoxOffice = " + responseDailyBoxOffice);

        if (responseDailyBoxOffice.getStatusCode() == HttpStatus.OK && responseDailyBoxOffice.getBody() != null) {
            DailyBoxOfficeResponse dailyBoxOfficeResponse = responseDailyBoxOffice.getBody();
            log.info("BoxofficeType = {} ",dailyBoxOfficeResponse.getBoxOfficeResult().getBoxofficeType());
            log.info("DailyBoxOfficeList = {} ",dailyBoxOfficeResponse.getBoxOfficeResult().getDailyBoxOfficeList());
            log.info("ShowRange = {} ",dailyBoxOfficeResponse.getBoxOfficeResult().getShowRange());

            return dailyBoxOfficeResponse;
        } else {
            log.error("Error calling DailyBoxOfficeResponse API: {}", responseDailyBoxOffice.getStatusCode());
            return null;
        }

    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding URL parameter", e);
            return "";
        }
    }

}
