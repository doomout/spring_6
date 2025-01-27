package com.example.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //컨트롤러
@EnableWebSecurity //스프링 시큐리티 사용
public class SecurityConfig {
    @Bean //빈으로 등록 관리
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/maintenance/**").hasRole("MANAGER") // POST 요청은 MANAGER 권한 필요
                        .requestMatchers("/maintenance/**").hasAnyRole("MANAGER", "EMPLOYEE") // 다른 요청은 MANAGER 또는 EMPLOYEE 권한 필요
                        .anyRequest().permitAll()) // 그 외 모든 요청은 허용
                .formLogin(login -> login
                        .loginPage("/login") // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/maintenance/product/display-list") // 로그인 성공 시 이동할 페이지
                        .failureUrl("/login?failure")) // 로그인 실패 시 이동할 페이지
                .exceptionHandling(handling -> handling
                        .accessDeniedPage("/display-access-denied")); // 권한이 없을 때 이동할 페이지

        return http.build(); // 설정 완료 후 SecurityFilterChain 객체를 반환
    }

    @Bean
    public UserDetailsService userDetailsService () {
        UserDetails taro = User.builder()
                .username("taro").password("{noop}taro123").roles("MANAGER").build(); // MANAGER 권한
        UserDetails jiro = User.builder()
                .username("jiro").password("{noop}jiro123").roles("EMPLOYEE").build(); // EMPLOYEE 권한
        UserDetails saburo = User.builder()
                .username("saburo").password("{noop}saburo123").roles("GUEST").build(); // GUEST 권한

        return new InMemoryUserDetailsManager(taro, jiro, saburo); // 메모리 내 사용자 정보 관리
    }
}
