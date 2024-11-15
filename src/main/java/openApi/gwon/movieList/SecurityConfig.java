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
        http.authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().permitAll() // 모든 경로에 대해 인증 없이 접근 허용
                ).formLogin(formLogin -> formLogin
                                .loginPage("/login") // 커스텀 로그인 페이지 경로 todo :
                                .permitAll()
                ).logout(logout ->
                        logout.logoutSuccessUrl("/") // 로그아웃 후 이동할 페이지 todo :
                                .permitAll()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin() // 동일 도메인 내에서 iframe 사용 허용
                        )
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();

        var user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);

        return userDetailsManager;
    }


}*/
