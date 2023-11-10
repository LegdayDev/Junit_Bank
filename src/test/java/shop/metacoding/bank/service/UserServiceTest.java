package shop.metacoding.bank.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.metacoding.bank.config.dummy.DummyObject;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserRepository;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static shop.metacoding.bank.dto.user.UserReqDto.*;
import static shop.metacoding.bank.dto.user.UserRespDto.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;

    @Mock // 가짜 객체 UserRepository 를 UserService 에 넣는다.
    private UserRepository userRepository;

    @Spy // 진짜 스프링 IOC 에 있는 빈을 꺼내서 가짜 UserService 에 넣는다.
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 테스트")
    public void 회원가입() throws Exception {
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("Ronaldo");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ronaldo@nate.com");
        joinReqDto.setFullName("Cristiano Ronaldo");

        // stub 필요
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // stub 필요
        User user = newMockUser(1L,"Ronaldo","Cristiano");
        when(userRepository.save(any())).thenReturn(user);

        //when
        JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        System.out.println("joinRespDto = " + joinRespDto);

        //then
        assertThat(joinRespDto.getId()).isEqualTo(1L);
        assertThat(joinRespDto.getUsername()).isEqualTo("Ronaldo");
        assertThat(joinRespDto.getFullName()).isEqualTo("Cristiano");
    }
}