package shop.metacoding.bank.config.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import shop.metacoding.bank.config.auth.LoginUser;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtProcessTest {

    @Test
    public void create_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);

        //when
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println(jwtToken);
        //then
        assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    public void verify_test() throws Exception {
        //given
        String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTcwMjAxOTgxMX0.H5KaW5an6nfVyG99wcXYOkmVq2w8FLeePh-gp66wIlccCavydg4T8mrvbx14i82Zgd2Ell5hc9kCDOIa5eCHbw";

        //when
        LoginUser loginUser = JwtProcess.verify(jwtToken);

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
    }
}