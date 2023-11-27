package shop.metacoding.bank.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import shop.metacoding.bank.config.auth.LoginUser;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;

import java.util.Date;

@Slf4j
public class JwtProcess {

    /**
     * <h2>JWT 토큰 생성 메서드</h2>
     * @return
     */
    public static String create(LoginUser loginUser){
        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtVO.EXPIRATION_TIME))
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole() + "")
                .sign(Algorithm.HMAC512(JwtVO.SECRET));
        return JwtVO.TOKEN_PREFIX+jwtToken;
    }

    /**
     * <h2>JWT 토큰 검증 메서드</h2>
     * <li>return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입</li>
     */
    public static LoginUser verify(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(token);

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        User user = User.builder().
                        id(id).
                        role(UserEnum.valueOf(role)).
                        build();
        LoginUser loginUser = new LoginUser(user);
        return loginUser;

    }

}
