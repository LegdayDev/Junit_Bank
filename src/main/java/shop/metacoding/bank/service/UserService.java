package shop.metacoding.bank.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.metacoding.bank.domain.user.User;
import shop.metacoding.bank.domain.user.UserEnum;
import shop.metacoding.bank.domain.user.UserRepository;
import shop.metacoding.bank.dto.user.UserReqDto;
import shop.metacoding.bank.dto.user.UserRespDto;
import shop.metacoding.bank.handler.ex.CustomApiException;

import java.util.Optional;

import static shop.metacoding.bank.dto.user.UserReqDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * <h2>회원가입 서비스</h2>
     * @param joinReqDto
     * @Return  joinRespDto
     */
    @Transactional
    public UserRespDto.JoinRespDto 회원가입(JoinReqDto joinReqDto){
        // 1. username 이 중복인지 검사
        Optional<User> userOptional = userRepository.findByUsername(joinReqDto.getUsername());
        if(userOptional.isPresent()){
            //username 중복
            throw new CustomApiException("동일한 username 이 존재합니다 !");
        }

        // 2. 패스워드 암호화 + 회원가입
        User userPS = userRepository.save(joinReqDto.toEntity(bCryptPasswordEncoder));

        // 3. DTO 응답
        return new UserRespDto.JoinRespDto(userPS);
    }





}
