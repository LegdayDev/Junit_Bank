package shop.metacoding.bank.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.metacoding.bank.config.jwt.JwtAuthenticationFilter;
import shop.metacoding.bank.config.jwt.JwtAuthorizationFilter;
import shop.metacoding.bank.domain.user.UserEnum;
import shop.metacoding.bank.dto.ResponseDto;
import shop.metacoding.bank.util.CustomResponseUtil;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("BCryptPasswordEncoder Bean 등록");
        return new BCryptPasswordEncoder();
    }

    // 공식 문서에 따라 모든 Filter 는 이 메서드에서 등록해야 한다.
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager,HttpSecurity>{
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            // 강제로 AuthenticationManager 를 만든다.
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); //HTTP <iframe> 허용안함
        http.csrf().disable(); // enable 이면 postman 작동안함
        http.cors().configurationSource(configurationSource()); // todo : CORS 는  JS 요청 거부

        // jSessionId 를 서버쪽에서 관리안한다는 뜻
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // react, 앱 요청을 받기 때문에 Form 으로 Login 을 안한다.
        http.formLogin().disable();
        // httpBasic 은 브라우저가 팝업창을 이용해서 사용자 인증을 진행하는 것을 막는다.
        http.httpBasic().disable();
        // Filter 적용
        http.apply(new CustomSecurityFilterManager());

        // Exception 가로채기(인증실패)
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.fail(response, "로그인을 진행해 주세요 !!", HttpStatus.UNAUTHORIZED);
        });

        // Exception 가로채기(권한 실패)
        http.exceptionHandling().accessDeniedHandler((request,response,e)->{
            CustomResponseUtil.fail(response, "관리자 권한이 없습니다.", HttpStatus.FORBIDDEN);
        });

        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated() // 인증필요
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN) // ROLE_ 안붙혀도됨
                .anyRequest().permitAll(); // 나머지 요청은 허용

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); // 모든 헤더허용
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 주소 허용
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 어떤 주소가 요청이 와도 위에 설정들을 다 셋팅한다.

        return source;
    }
}