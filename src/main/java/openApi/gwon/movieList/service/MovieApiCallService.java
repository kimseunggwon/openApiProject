package openApi.gwon.movieList.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.security.jarsigner.JarSignerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openApi.gwon.movieList.OpenApiConstants;
import openApi.gwon.movieList.dto.WeeklyBoxOfficeList.WeeklyBoxOfficeListDto;
import openApi.gwon.movieList.repository.MovieListImplRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


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


}
