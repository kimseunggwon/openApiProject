package openApi.gwon.movieList.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.security.jarsigner.JarSignerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.OpenApiConstants;
import openApi.gwon.movieList.dto.MovieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class MovieApiCallService {

    private final MovieListImplRepository movieListImplRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    /**
     * 주간/주말 박스오피스 API 서비스
     */
    public ResponseEntity<String> callWeeklyBoxOfficeApi(String targetDt, String weekGb, String repNationCd) throws JarSignerException {

        // UriComponentsBuilder 사용시 = 쿼리파라미터 추가할 때 자동으로 URL 올바른 형식으로 구현해줌 ( ?,& )
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUri(URI.create(OpenApiConstants.API_URL_WEEKLY_BOX_OFFICE))
                .queryParam("key", OpenApiConstants.API_KEY_LIST)
                .queryParam("targetDt", targetDt); // @쿼리파라미터할지 @pathvaribale 할지

        // 주간/주말/주중을 선택
        if (weekGb != null) {
            builder.queryParam("weekGb", weekGb);
        }

        // 한국/외국 영화별
        if (repNationCd != null) {
            builder.queryParam("repNationCd", repNationCd);
        }

        String url = builder.toUriString();

        //API 호출
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            log.info("responseWeeklyBoxOffice = " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return response; // JSON 문자열 그대로 반환
            } else {
                // 에러가 포함된 응답을 받을 경우 에러 메시지 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode faultInfo = rootNode.path("faultInfo");

                //특장 에러 코드 있는지 검사
                if (!faultInfo.isMissingNode() && faultInfo.has("errorCode")) {
                    String errorCode = faultInfo.get("errorCode").asText();
                    if ("320107".equals(errorCode)) {
                        // 에러 코드 320107 처리
                        log.error("국적구분 조건 에러: {}", errorCode);
                        return ResponseEntity.badRequest().body(faultInfo.get("message").asText());
                    }
                }
                // 기타 상황에서의 기본 반환값
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 에러가 발생했습니다.");
            }
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException when calling Weekly Box Office API", e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Exception when calling Weekly Box Office API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API 호출 중 오류가 발생했습니다.");
        }
    }

    /** 영화목록 조회 API 서비스
     */
    public List<MovieListDto> callMovieListApi() throws Exception {
        final int ITEMS_PER_PAGE = 10; // 한 페이지에 요청할 영화의 개수를 지정
        final int MAX_ITEMS = 500;
        List<MovieListDto> allMovies = new ArrayList<>();

       // String firstPageUrl = buildUrl(1, ITEMS_PER_PAGE).toUriString();
        //log.info("firstPageUrl" + firstPageUrl);
        //ResponseEntity<String> firstResponse = restTemplate.getForEntity(firstPageUrl, String.class);
        //log.info("firstResponse" + firstResponse);

        //int totalItems = extractTotalCount(firstResponse.getBody()); // 전체 데이터 개수
        //log.info("totalItems" + totalItems);
        //전체 데이터 개수를 페이지 당 데이터 개수로 나눈 값 전체 데이터 : 100,822 / 페이지 당 : 10  = 10083 페이지 ( 소수점 올림 )
        //int totalPages = (int)Math.ceil((double)totalItems / ITEMS_PER_PAGE);
        //log.info("totalPages" + totalPages);

        int totalPages = (int) Math.ceil((double)MAX_ITEMS / ITEMS_PER_PAGE);

        // ITEMS_PER_PAGE = 한 페이지에 10개씩 , page = 5개면 , 총 50개
        for (int page = 1; page <= totalPages; page++) { //  page <= totalPages
            log.info("page" + page);
            String url = buildUrl(page, ITEMS_PER_PAGE).toUriString();
            log.info("url : " + url);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("response.getBody" + response.getBody());
                allMovies.addAll(extractMovies(response.getBody()));
                System.out.println("allMovies" + allMovies);
            }
        }
       /* if (firstResponse.getStatusCode().is2xxSuccessful()) {
            allMovies.addAll(extractMovies(firstResponse.getBody()));
            log.info("allMovies.size : " + allMovies.size());
        }*/
        return allMovies;
    }

    private UriComponentsBuilder buildUrl(int currentPage, int itemsPerPage) {
        return UriComponentsBuilder
                .fromUriString(OpenApiConstants.API_URL_MOVIE_LIST)
                .queryParam("key", OpenApiConstants.API_KEY_LIST)
                .queryParam("curPage" , currentPage)
                .queryParam("itmPerPage" , itemsPerPage);
    }


    private int extractTotalCount (String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> resultMap = objectMapper.readValue(responseBody, HashMap.class);
        HashMap<String, Object> movieListResult = (HashMap<String, Object>) resultMap.get("movieListResult");
        if (movieListResult != null) { // movieListResult 가 null 이 아닐 때만 접근
            return (Integer) movieListResult.get("totCnt");
        } else {
            // 적절한 예외를 던지거나, 오류 처리
            log.error("movieListResult is null");
            throw new Exception("movieListResult is null");
        }
    }

    private List<MovieListDto> extractMovies(String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<MovieListDto> movieListDtos = new ArrayList<>();


        HashMap<String, Object> resultMap = objectMapper.readValue(responseBody, HashMap.class);
        HashMap<String, Object> movieListResult = (HashMap<String, Object>) resultMap.get("movieListResult");
        // NullPointerException 에러 방지
        if (movieListResult == null) {
            return movieListDtos;
        }

        List<HashMap<String, Object>> movieList = (List<HashMap<String, Object>>) movieListResult.get("movieList");
        for (HashMap<String, Object> movie : movieList) {
            MovieListDto movieDto = new MovieListDto();
            movieDto.setMovieListId(UUID.randomUUID().toString());
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

            // 직렬화 처리
            String directorsJson = objectMapper.writeValueAsString(movie.get("directors"));
            String companiesJson = objectMapper.writeValueAsString(movie.get("companys"));
            movieDto.setDirectorsJson(directorsJson);
            movieDto.setCompaniesJson(companiesJson);

            movieListDtos.add(movieDto);
        }
        return movieListDtos;
    }




}
