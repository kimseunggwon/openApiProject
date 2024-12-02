package openApi.gwon.movieList.config;


import openApi.gwon.movieList.cmmn.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 인터셉터 등록

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(new LoginInterceptor())
              .addPathPatterns("/main/**") // main 경로 하위의 모든 요청에 대해 인터셉터 적용
              .excludePathPatterns("/login.do", "/register.do", "/findId.do", "/findPw.do");  // 로그인 및 회원가입 페이지는 제외
    }
}
