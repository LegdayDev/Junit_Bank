package shop.metacoding.bank.config.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import shop.metacoding.bank.config.auth.LoginUser;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtProcessTest {

    private String createToken(){
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        return JwtProcess.create(loginUser);
    }

    @Test
    public void create_test() throws Exception {
        //given

        //when
        String jwtToken = createToken();
        System.out.println(jwtToken);
        //then
        assertThat(jwtToken.startsWith(JwtVO.TOKEN_PREFIX)).isTrue();
    }

    @Test
    public void verify_test() throws Exception {
        //given
        String jwtToken = createToken().replace(JwtVO.TOKEN_PREFIX, "");


        //when
        LoginUser loginUser = JwtProcess.verify(jwtToken);

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
    }
}