package openApi.gwon.movieList.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.OpenApiConstants;
import openApi.gwon.movieList.dto.DailyBoxOfficeList.DailyBoxOfficeListDto;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieApiGetService {

    private final MovieListImplRepository movieListImplRepository;

    private final MovieApiCallService movieApiCallService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public MovieListDto findByCd(String movieCd) {
        return movieListImplRepository.findByCd(movieCd);
    }

    public List<MovieListDto> findAllMovies() {

        int countMovies = movieListImplRepository.countAllMovies();
        log.info("countMovies = "+ countMovies);

        return movieListImplRepository.findAll();
    }

    /** 영화목록 조회 API 서비스 저장
     */
    public List<MovieListDto> saveMovieApi(String movieNm) throws Exception{
        List<MovieListDto> movieListDtos = callMovieApi(movieNm);

        int countMovies = movieListImplRepository.countAllMovies();
        log.info("countMovies = "+ countMovies);

        int insertMoive = movieListImplRepository.insertMovies(movieListDtos);
        log.info("{} rows inserted into database", insertMoive);
        return movieListDtos;
    }

    public void testSaveMoive() throws Exception {
      List<MovieListDto> movieListDtos = movieApiCallService.callMovieListApi();

      //실제 DB에 저장될 영화 목록
      List<MovieListDto> toBeSave = new ArrayList<>();

      // 최대 100개까지만 저장
      int maxMoviesToSave = 100;

      for (MovieListDto movie : movieListDtos) {
          if (toBeSave.size() >= maxMoviesToSave) break;
          toBeSave.add(movie);
          // 중복체크 로직은 나중에
          log.info("toBeSave" +toBeSave);
      }
        movieListImplRepository.insertMovies(toBeSave);
        log.info(toBeSave.size() + " movies saved to the database.");

    }



    /** 일별 박스오피스 API 서비스 저장
     */
    public List<DailyBoxOfficeListDto> saveDailyBoxOfficeApi(String targetDt) throws JsonProcessingException {

        List<DailyBoxOfficeListDto> dailyBoxOfficeListDtos = null; // 변수 선언

        try {
            dailyBoxOfficeListDtos = callDailyBoxOfficeApi(targetDt);
            log.info("targetDt a" + targetDt);

            int insertDailyBoxOfiice = movieListImplRepository.insertDailyBoxOffice(dailyBoxOfficeListDtos);
            log.info("{} rows inserted into database", insertDailyBoxOfiice);

        } catch (DuplicateKeyException e) {
            log.info("--------------------");
            log.info("중복된 데이터 입니다");
            log.info("--------------------");
        }

        return dailyBoxOfficeListDtos;
    }


    /** 20240101 ~ now 원하는 날짜 저장
     */
    public void saveDailyBoxOfficeForPeriod() throws JsonProcessingException {
        LocalDate startDate = LocalDate.of(2024,1,1);
        LocalDate endDate = LocalDate.now();

        // 설정한 기간 동안 각 날짜에 대해 반복, 루프의 각 단계에서 하루씩 날짜 증가
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)){
            log.info("startDate " + startDate);
            log.info("endDate " + endDate);
            log.info("date " + date);
            // 날짜를 yyMMdd 형식으로 포매팅
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            log.info("formattedDate a" + formattedDate);
            // 포매팅된 날짜를 사용해 API 호출 및 저장
            //List<DailyBoxOfficeListDto> result = saveDailyBoxOfficeApi(formattedDate);
            //log.info("formattedDate result " + result);

            //추후 result를 이용해 추가적인 처리

        }
    }

    /**
     * 영화목록 조회 API 서비스
     * @param movieNm
     * @return MovieListDto
     * @throws Exception
     */
    public List<MovieListDto> callMovieApi(String movieNm) throws Exception{

        // itemPerPage= 페이지네이션 지정
        String url = OpenApiConstants.API_URL_MOVIE_LIST 
                + "?key=" + OpenApiConstants.API_KEY_LIST 
                + "&movieNm=" 
                + movieNm +"&itemPerPage=20";
        log.info("MOVIE_LIST url = " + url);

        // UriComponentsBuilder 클래스 자동인코딩 이슈로 보류
        /* String url = UriComponentsBuilder.fromHttpUrl(OpenApiConstants.API_URL_MOVIE_LIST)
                .queryParam("key", OpenApiConstants.API_KEY_LIST)
                .queryParam("movieNm", movieNm)
                .toUriString();*/

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("reponse =  " + response);

        int countMovies = movieListImplRepository.countAllMovies();
        log.info("countMovies = "+ countMovies);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null){
            String responseBody = response.getBody();

                HashMap<String, Object> resultMap = objectMapper.readValue(responseBody,HashMap.class);
                HashMap<String, Object> movieListResult = (HashMap<String, Object>) resultMap.get("movieListResult");
                log.info("totCnt = " +  movieListResult.get("totCnt"));
                Integer totCnt = (Integer) movieListResult.get("totCnt");
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
                    movieDto.setTotCnt(totCnt);

                    String directorsJson = objectMapper.writeValueAsString(movie.get("directors"));
                    String companiesJson = objectMapper.writeValueAsString(movie.get("companys"));
                    movieDto.setDirectorsJson(directorsJson);
                    movieDto.setCompaniesJson(companiesJson);

                    movieListDtos.add(movieDto);
                }
                return movieListDtos;

        }  else {
            throw new RuntimeException("Failed to call Movie API: " + response.getStatusCode());
        }
    }

    /**
     * 일별 박스오피스 API 서비스
     * @param targetDt
     * @return
     */
    public List<DailyBoxOfficeListDto> callDailyBoxOfficeApi(String targetDt) throws JsonProcessingException {

        String url = OpenApiConstants.API_URL_DAILY_BOX_OFFICE 
                + "?key=" + OpenApiConstants.API_KEY_LIST 
                + "&targetDt=" + targetDt;
               /* + "&itemPerPage=" + itemPerPage; // default 10
        /*String url = UriComponentsBuilder.fromHttpUrl(OpenApiConstants.API_URL_DAILY_BOX_OFFICE)
                .queryParam("?key", OpenApiConstants.API_KEY_LIST)
                .queryParam("&targetDt=", targetDt)
                .toUriString();
        log.info("DAILY_BOX_OFFICE url  = " + url);*/

        //ResponseEntity<DailyBoxOfficeResponse> responseDailyBoxOffice = restTemplate.getForEntity(url, DailyBoxOfficeResponse.class);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("responseDailyBoxOffice = " + response.getBody().length());

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            // TypeReference :
            Map<String,Object> resultMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            Map<String,Object> boxOfficeResult = (Map<String, Object>) resultMap.get("boxOfficeResult");
            // list Map
            List<Map<String,Object>> dailyBoxOfficeList = (List<Map<String, Object>>) boxOfficeResult.get("dailyBoxOfficeList");

            List<DailyBoxOfficeListDto> dailyBoxOfficeListDtos = new ArrayList<>();
            for (Map<String,Object> dailyBoxOffice: dailyBoxOfficeList) {
                DailyBoxOfficeListDto dto = new DailyBoxOfficeListDto();
                dto.setDailyBoxOfficeId(UUID.randomUUID().toString());
                dto.setRnum((String) dailyBoxOffice.get("rnum"));
                dto.setRank((String) dailyBoxOffice.get("rank"));
                dto.setRankInten((String) dailyBoxOffice.get("rankInten"));
                dto.setRankOldAndNew((String) dailyBoxOffice.get("rankOldAndNew"));
                dto.setMovieCd((String) dailyBoxOffice.get("movieCd"));
                dto.setMovieNm((String) dailyBoxOffice.get("movieNm"));
                dto.setOpenDt((String) dailyBoxOffice.get("openDt"));
               /* dto.setSalesAmt((long) Integer.parseInt((String) dailyBoxOffice.get("salesAmt")));*/
                dto.setSalesAmt(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("salesAmt"))));
                dto.setSalesShare((String) dailyBoxOffice.get("salesShare"));
                dto.setSalesInten(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("salesInten"))));
                dto.setSalesChange((String) dailyBoxOffice.get("salesChange"));
                dto.setSalesAcc(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("salesAcc"))));
                dto.setAudiCnt(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("audiCnt"))));
                dto.setAudiInten(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("audiInten"))));
                dto.setAudiChange((String) dailyBoxOffice.get("audiChange"));
                dto.setAudiAcc(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("audiAcc"))));
                dto.setScrnCnt(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("scrnCnt"))));
                dto.setShowCnt(String.valueOf(Long.parseLong((String) dailyBoxOffice.get("showCnt"))));

                dailyBoxOfficeListDtos.add(dto);
            }

            return dailyBoxOfficeListDtos;
        } else {
            log.error("Error calling Daily Box Office API: {}", response.getStatusCode());
            throw new RuntimeException("Error calling Daily Box Office API");
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
