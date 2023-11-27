package shop.metacoding.bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.metacoding.bank.config.auth.LoginUser;
import shop.metacoding.bank.dto.user.UserReqDto;
import shop.metacoding.bank.dto.user.UserRespDto;
import shop.metacoding.bank.util.CustomResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static shop.metacoding.bank.dto.user.UserReqDto.*;
import static shop.metacoding.bank.dto.user.UserRespDto.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login"); // 디폴트였던 /login 요청이 아닌 /api/login 요청으로 변경
        this.authenticationManager = authenticationManager;
    }

    // POST : /login 요청 시 동작
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication() 호출됨");
        try {
            ObjectMapper om = new ObjectMapper();
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

            // 강제 로그인을 위한 토큰
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginReqDto.getUsername(),loginReqDto.getPassword());

            // UserDetailsService 클래스의 loadUserByUsername() 호출(강제로그인)
            // 강제 로그인을 하는 이유는 시큐리티 설정파일에서 설정했던 권한체크의 도움을 받기 위해
            // 이 세션의 유효기간은 request -> response 하면 끝.(오직 권한체크만을 위한 세션)
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        }catch (Exception e){
            // unsuccessfulAuthentication() 호출
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // attemptAuthentication() 에서 로그인 실패 시 호출
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.unAuthentication(response,"로그인 실패 !");
    }

    // attemptAuthentication() 에서 로그인 성공 시 호출
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("디버그 : successfulAuthentication() 호출됨");
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(loginUser);

        response.addHeader(JwtVO.HEADER, jwtToken);

        LoginRespDto loginRespDto = new LoginRespDto(loginUser.getUser());

        CustomResponseUtil.success(response,loginRespDto);
    }
}
