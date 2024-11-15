package openApi.gwon.movieList.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.security.jarsigner.JarSignerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.OpenApiConstants;
import openApi.gwon.movieList.dto.companyDetail.CompanyDetailsDto;
import openApi.gwon.movieList.dto.companyDetail.FilmoDto;
import openApi.gwon.movieList.dto.companyDetail.PartDto;
import openApi.gwon.movieList.dto.movieDetail.*;
import openApi.gwon.movieList.dto.movieList.CompanyList;
import openApi.gwon.movieList.dto.movieList.MovieListDto;
import openApi.gwon.movieList.repository.MovieCompanyDetailImplRepository;
import openApi.gwon.movieList.repository.MovieDetailImplRepository;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MovieApiCallService {

    private final MovieListImplRepository movieListImplRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final MovieDetailImplRepository movieDetailImplRepository;
    private final MovieCompanyDetailImplRepository movieCompanyDetailImplRepository;


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
            log.info("callWeeklyBoxOfficeApi a = " , url);
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

    /**
     * 영화목록 조회 API 서비스
     */
    public List<MovieListDto> callMovieListApi() throws Exception {
        final int ITEMS_PER_PAGE = 10; // 한 페이지에 요청할 영화의 개수를 지정
        final int MAX_ITEMS = 1000;
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

        int totalPages = (int) Math.ceil((double) MAX_ITEMS / ITEMS_PER_PAGE); // 500 / 10 => 10개씩 50 페이지 => 총 500개
        //log.info("totalPages aa" + totalPages);

        // ITEMS_PER_PAGE = 한 페이지에 10개씩 , page = 5개면 , 총 50개
        for (int page = 1; page <= totalPages; page++) { //  page <= totalPages
            //log.info("page" + page);
            String url = buildUrl(page, ITEMS_PER_PAGE).toUriString();
            //log.info("url : " + url);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                //log.info("response.getBody" + response.getBody());
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
                .queryParam("curPage", currentPage)
                .queryParam("itmPerPage", itemsPerPage);
    }

    private int extractTotalCount(String responseBody) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> resultMap = objectMapper.readValue(responseBody, HashMap.class);
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
            movieDto.setDirectors(directorsJson);
            movieDto.setCompanys(companiesJson);

            movieListDtos.add(movieDto);
        }
        return movieListDtos;
    }

    /**
     * 영화 상세정보 호출 API
     * movieCd	문자열(필수) :	영화코드
     */
    @Transactional
    public MovieDetailDto callMovieDetailApi(String movieCd) throws Exception {

        UriComponentsBuilder urlBuilder = UriComponentsBuilder
                .fromUriString(OpenApiConstants.API_URL_MOVIE_DETAIL)
                .queryParam("key", OpenApiConstants.API_KEY_LIST)
                .queryParam("movieCd", movieCd);

        String url = urlBuilder.toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new Exception("API 호출 실패");
        }
        //log.info("response.getBody = {}", response.getBody());

        // ObjectMapper 인스턴스 생성
        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = response.getBody();
        JsonNode rootNode = objectMapper.readTree(responseBody);

        // 영화 상세 정보 추출
        JsonNode movieInfoNode = rootNode.path("movieInfoResult").path("movieInfo");
        MovieDetailDto movieDetail = objectMapper.treeToValue(movieInfoNode, MovieDetailDto.class);
        //log.info("movieDetail = {}", movieDetail);

        // 관련된 DTO를 리스트로 추출
        List<Actor> actors = objectMapper.readValue(
                movieInfoNode.path("actors").toString(),
                new TypeReference<List<Actor>>() {
                }

        );

        List<Company> companies = objectMapper.readValue(
                movieInfoNode.path("companys").toString(),
                new TypeReference<List<Company>>() {
                }
        );

        List<ShowType> showTypes = objectMapper.readValue(
                movieInfoNode.path("showTypes").toString(),
                new TypeReference<List<ShowType>>() {
                }
        );

        List<Staff> staffs = objectMapper.readValue(
                movieInfoNode.path("staffs").toString(),
                new TypeReference<List<Staff>>() {
                }
        );

        //배열 JSON 형태 필드 값을 추출하여 문자열로 결합  = "nations":[{"nationNm":"독일"}] , "genres":[{"genreNm":"전쟁"},{"genreNm":"드라마"}]
        JsonNode nationsNode = movieInfoNode.path("nations");
        StringBuilder nationNamesBuilder = new StringBuilder();
        for (JsonNode nationNode : nationsNode) {
            if (nationNamesBuilder.length() > 0) nationNamesBuilder.append(",");
            nationNamesBuilder.append(nationNode.path("nationNm").asText());
        }
        movieDetail.setNationNm(nationNamesBuilder.toString());
        //log.info("setNationNm = {}" , movieDetail.getNationNm());

        JsonNode genresNode = movieInfoNode.path("genres");
        StringBuilder genreNamesBuilder = new StringBuilder();
        for (JsonNode genreNode : genresNode) {
            if (genreNamesBuilder.length() > 0) genreNamesBuilder.append(", ");
            genreNamesBuilder.append(genreNode.path("genreNm").asText());
        }
        movieDetail.setGenreNm(genreNamesBuilder.toString());
        //log.info("setGenreNm = {}" , movieDetail.getGenreNm());


        // MovieDetail 객체 movieCd 공유
        String movieCode = movieDetail.getMovieCd();

        setMovieCdToRelatedEntities(actors, movieCode);
        setMovieCdToRelatedEntities(companies, movieCode);
        setMovieCdToRelatedEntities(showTypes, movieCode);
        setMovieCdToRelatedEntities(staffs, movieCode);

        movieDetail.setActors(actors);
        movieDetail.setCompanies(companies);
        movieDetail.setShowTypes(showTypes);
        movieDetail.setStaffs(staffs);
        //log.info("actors = {}", actors);
        //log.info("companies = {}", companies);
        //log.info("showTypes = {}", showTypes);
        //log.info("staffs = {}", staffs);

        return movieDetail;
    }


    // Actor, Company, ShowType, Staff 클래스들의 객체는 모두 MovieDetail 객체에 연관된 객체로 같은 영화 코드를 공유해야한다.
    // 문제는 ObjectMapper를 사용하여 JSON 응답을 Actor 객체 리스트로 직접 변환하려고 할 때 movieCd 값이 자동으로 설정되지 않는다는 것 ( 최상위에 있는 MovieDetail에서만 제공된다 )
    // 이를 해결하려면, Actor 리스트를 생성한 후 각 Actor 객체에 영화 코드를 수동으로 설정해 주어한다
    private <T extends MovieRelatedEntity> void setMovieCdToRelatedEntities(List<T> entities, String movieCd) {
        for (T entity : entities) {
            entity.setMovieCd(movieCd);
        }
    }

    /**
     * 영화 상세보기 insert 작업
     * KEY : MoiveCd
     */
    @Transactional
    public void fetchSaveMovieDetails() {

        // 저장된 영화 코드 추출 ( 테스트 movieCode 10개만 우선 )
        //List<String> movieCodes = movieListImplRepository.findAllMovieCodes().subList(0, 10);
        List<String> movieCodes = movieListImplRepository.findAllMovieCodes();
        //log.info("Processing movieCodes  = {}", movieCodes);

        // todo : 중복 데이터 처리
        for (String movieCd : movieCodes) {
            try {

                //각 영화 코드에 대한 전체 영화 상세 정보 조회
                MovieDetailDto movieDetail = callMovieDetailApi(movieCd);
                //log.info("movieCd 에 대한 movieDetail = {}", movieDetail);

                // 조회된 영화 상세 정보 저장
                movieDetailImplRepository.saveMovieDetail(movieDetail); // 영화 기본 정보 저장

                // 관련 엔티티들도 저장
                for (Actor actor : movieDetail.getActors()) {
                    movieDetailImplRepository.saveActor(actor);
                    //log.info("actor save  = {} " , actor);
                }

                for (Company company : movieDetail.getCompanies()) {
                    movieDetailImplRepository.saveCompany(company);
                    //log.info("company save  = {} " , company);
                }

                for (ShowType showType : movieDetail.getShowTypes()) {
                    movieDetailImplRepository.saveShowType(showType);
                    //log.info("showType save  = {} " , showType);
                }

                for (Staff staff : movieDetail.getStaffs()) {
                    movieDetailImplRepository.saveStaff(staff);
                    //log.info("staff save  = {} " , staff);
                }
                // 기타 필요한 엔티티 저장 작업

            } catch (Exception e) {
                log.error("Error processing movieCd {}: ", movieCd, e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 영화 상세정보 호출 API
     * movieCd	문자열(필수) :	영화코드
     */
    public MovieDetailDto getMovieDetailByMovieCd(String movieCd) {

        MovieDetailDto movieDetail =  movieDetailImplRepository.findMovieDetailByMovieCd(movieCd);

        if (movieDetail != null) {
            // 빈 컬렉션 처리
            movieDetail.setActors(filterEmpty(movieDetail.getActors()));
            movieDetail.setCompanies(filterEmpty(movieDetail.getCompanies()));
            movieDetail.setShowTypes(filterEmpty(movieDetail.getShowTypes()));
            movieDetail.setStaffs(filterEmpty(movieDetail.getStaffs()));

            return movieDetail;
        } else {
            // 조회된 정보가 없다면, 예외 처리나 적절한 로직을 추가 예정
            throw new RuntimeException("영화 상세 정보를 찾을 수 없습니다. 영화 코드: " + movieCd);
        }
    }

    private <T> List<T> filterEmpty(List<T> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .filter(item -> !isEmpty(item))
                .collect(Collectors.toList());
    }

    /** null ,empty 처리
     */
    private boolean isEmpty(Object object) {
        if (object instanceof Actor) {
            Actor actor = (Actor) object;
            return actor.getPeopleNm() == null && actor.getPeopleNmEn() == null && actor.getCastName() == null && actor.getCastEn() == null;
        } else if (object instanceof Company) {
            Company company = (Company) object;
            return company.getCompanyCd() == null && company.getCompanyNm() == null && company.getCompanyNmEn() == null && company.getCompanyPartNm() == null;
        } else if (object instanceof ShowType) {
            ShowType showType = (ShowType) object;
            return showType.getShowTypeGroupNm() == null && showType.getShowTypeNm() == null && showType.getAuditNo() == null && showType.getWatchGradeNm() == null;
        } else if (object instanceof Staff) {
            Staff staff = (Staff) object;
            return staff.getPeopleNm() == null && staff.getPeopleNmEn() == null && staff.getStaffRoleNm() == null;
        }
        return false; // 타입이 매칭되지 않는 경우, 기본적으로는 비어있지 않다고 가정
    }

    /**
     * 영화사 상세정보 조회 API
     * @param companyCd 영화사코드
     * @param movieListId 영화 목록 ID
     * @return CompanyDetailsDto 변환된 회사 상세 정보 DTO
     * @throws Exception API 호출 실패 또는 JSON 파싱 실패시 예외 발생
     */
    public CompanyDetailsDto callCompanyDetail(String companyCd, String movieListId) throws Exception {
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                    .fromUriString(OpenApiConstants.API_URL_COMPANY_DETAIL)
                    .queryParam("key", OpenApiConstants.API_KEY_LIST)
                    .queryParam("companyCd", companyCd);

            String url = uriComponentsBuilder.toUriString();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new Exception("API 호출 실패");
            }
            log.info("response.getBody = {}", response.getBody());

        // ObjectMapper를 사용하여 JSON 응답에서 "companyInfo" 객체를 추출
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode companyInfoNode = rootNode.path("companyInfoResult").path("companyInfo");

        // JsonNode에서 DTO로 변환
        CompanyDetailsDto companyDetails = objectMapper.treeToValue(companyInfoNode,CompanyDetailsDto.class);
        companyDetails.setMovieListId(movieListId); // movieListId 설정
        companyDetails.setCompanyDetailCd(companyCd);
        //log.info("companyDetails = {}" , companyDetails);

        if (companyDetails.getParts() != null){
            for (PartDto part : companyDetails.getParts()){
                part.setPartCompanyCd(companyCd);
            }
        }
        if (companyDetails.getFilmos() != null) {
            for (FilmoDto filmo : companyDetails.getFilmos()) {
                filmo.setFilmoCompanyCd(companyCd);

                //문자열을 리스트로 반환
                List<String> partNames = Arrays.asList(filmo.getFilmoCompanyPartNm().split(","));
                filmo.setFilmoCompanyPartNm(partNames.toString());
            }
        }
        log.info("companyDetails = {}" , companyDetails);

        return companyDetails;
    }


    /** 1.MovieListDto의 목록을 얻고 companyCd를 추출하는 메서드
     */
    public List<Map.Entry<String,String>> extractCompanyCdFromMovieList() throws Exception {
        List<MovieListDto> moviesList = movieListImplRepository.findAll();
        log.info("moviesList = {}" , moviesList);
        List<Map.Entry<String,String>> companyCdList = new ArrayList<>();

        for (MovieListDto movie : moviesList) {
            if (movie.getCompanys() != null) {
                List<CompanyList> companyLists = parseCompanys(movie.getCompanys());
                for (CompanyList companyList : companyLists) {
                    companyCdList.add(new AbstractMap.SimpleEntry<>(movie.getMovieListId(), companyList.getCompanyCd()));
                }
            }
        }
        log.info("첫번째 companyCdList = {}" , companyCdList);
        return companyCdList;

    }
    public List<CompanyList> parseCompanys(String companysJson) throws Exception {
        // objectMapper.readValue 메서드의 두 번째 인자는 TypeReference를 이용해 실제 클래스의 타입을 명시해야 합니다.
        // 'companys'가 아닌 'CompanyList' 클래스 타입을 제네릭에 사용해야 합니다.
        return objectMapper.readValue(companysJson, new TypeReference<List<CompanyList>>(){});
    }

    /**
     * 실제 데이터베이스에 CompanyDetailsDto 객체를 저장하는 메서드
     * @param companyDetails 변환된 회사 상세 정보 DTO
     */
    @Transactional
    public void saveCompanyDetails(CompanyDetailsDto companyDetails) {

        // CompanyDetails 정보 저장
        movieCompanyDetailImplRepository.saveMovieDetail(companyDetails);

        // filmo 정보 저장
        if (companyDetails.getFilmos() != null) {
            for (FilmoDto filmo : companyDetails.getFilmos()) {
                filmo.setFilmoCompanyCd(companyDetails.getCompanyDetailCd());

                // 파트명 리스트를 적절한 문자열로 반환
                String partNamesAsString = String.join(", ", filmo.getFilmoCompanyPartNm());
                filmo.setFilmoCompanyPartNm(partNamesAsString);
                movieCompanyDetailImplRepository.insertFilmo(filmo);
            }
        }

        // part 정보 저장
        if (companyDetails.getParts() != null) {
            for (PartDto part : companyDetails.getParts()){
                part.setPartCompanyCd(companyDetails.getCompanyDetailCd());
                movieCompanyDetailImplRepository.insertCompanyPart(part);
            }
        }
    }


    /**
     *  모든 MovieListDto에서 companyCd를 추출하고, 각 companyCd에 대해 상세 정보를 조회한 후 저장
     */
    @Transactional
    public void processAndSaveCompanyDetails() throws Exception {
        List<Map.Entry<String, String>> companyCdList = extractCompanyCdFromMovieList();
        int count = 0;
        for (Map.Entry<String,String> entry : companyCdList) {
            if (count >= 100) break; // 5개 이상 처리 x
            String movieListId = entry.getKey();
            String companyCd = entry.getValue();
            log.info("movieListId a = {} " , movieListId);
            log.info("companyCd a = {} " , companyCd);

            try {
                CompanyDetailsDto companyDetails = callCompanyDetail(companyCd,movieListId);
                log.info("companyDetails = {}" , companyDetails);
                saveCompanyDetails(companyDetails);
                count++;
            } catch (Exception e) {
                log.error("Error processing company details for companyCd: {}, error: {}", companyCd, e.getMessage());
                continue; // 에러가 발생한 경우 ,다음 회사 상세 정보 처리로 넘어감
            }
        }
    }

    /**
     * MovieListDto 추출: movieListImplRepository.findAll()를 호출하여 MovieListDto의 목록을 가져옵니다. 각 MovieListDto에서 companyCd를 추출하고 이를 movieListId와 함께 저장합니다. 이 때 companyCd는 null일 수도 있고, 여러 개가 있을 수도 있습니다.
     *
     * API 호출 및 상세 정보 저장: 추출한 companyCd를 이용하여 callCompanyDetail 메서드로 API를 호출하고, 그 결과를 받습니다. API로부터 받은 데이터는 CompanyDetailsDto 객체로 변환되어야 하며, 이 과정에서 movieListId도 함께 저장되어야 합니다. 이렇게 함으로써 각 companyCd가 어느 movieListId에 속하는지 추적할 수 있습니다.
     *
     * 데이터베이스에 저장: 변환된 CompanyDetailsDto 객체들을 데이터베이스에 저장합니다. 이 때, companyCd와 movieListId의 연결 정보도 함께 저장되어야 하므로, 이를 위한 적절한 데이터베이스 설계가 필요합니다. 예를 들어, CompanyDetails 테이블에 movieListId 컬럼을 추가하여, 어떤 movieList의 companyCd인지를 명시할 수 있습니다.
     */


}
