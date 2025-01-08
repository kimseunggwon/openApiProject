package openApi.gwon.movieList.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@Slf4j
public class MailgunService {

    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.from}")
    private String from;

    public boolean sendEmail(String to, String subject, String text) {
        log.info("메일 전송 요청 - 수신자: {}, 제목: {}, 내용: {}", to, subject, text);

        String url = "https://api.mailgun.net/v3/" + domain + "/messages";
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);

            // 인증 설정
            String auth = "api:" + apiKey;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            post.setHeader("Authorization", "Basic " + encodedAuth);
            log.info("Encoded Authorization Header: {}", encodedAuth);

            // 요청 파라미터 추가 (URL 인코딩 적용)
            StringEntity params = new StringEntity(
                    "from=" + URLEncoder.encode(from, StandardCharsets.UTF_8) +
                            "&to=" + URLEncoder.encode(to, StandardCharsets.UTF_8) +
                            "&subject=" + URLEncoder.encode(subject, StandardCharsets.UTF_8) +
                            "&text=" + URLEncoder.encode(text, StandardCharsets.UTF_8),
                    StandardCharsets.UTF_8
            );
            post.setEntity(params);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            log.info("HTTP POST 요청 파라미터: {}", params);

            // 요청 실행
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

                log.info("응답 코드: {}", statusCode);
                log.info("응답 본문: {}", responseBody);

                return statusCode == 200;
            }
        } catch (IOException e) {
            log.error("메일 전송 실패: {}", e.getMessage(), e);
            return false;
        }
    }
}
