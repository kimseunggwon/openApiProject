/*
package openApi.gwon.movieList;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login.do", "/register.do", "/findId.do", "/findPw.do").permitAll() // 로그인 및 회원가입 페이지는 접근 허용
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login.do") // 커스텀 로그인 페이지 설정
                        .loginProcessingUrl("/perform_login") // 로그인 폼 action URL 설정
                        .defaultSuccessUrl("/main/boxOfficeList.do", true) // 로그인 성공 시 이동할 페이지
                        .failureUrl("/login.do?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/login.do") // 로그아웃 후 이동할 페이지
                        .permitAll()
                );
                //.csrf(csrf -> csrf.disable()); // CSRF 비활성화 (필요 시 활성화)

        return http.build();
    }




}
*/
