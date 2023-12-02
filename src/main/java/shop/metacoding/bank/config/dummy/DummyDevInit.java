package shop.metacoding.bank.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import shop.metacoding.bank.domain.user.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject{

    @Profile("dev") // dev 모드에서만 실행
    @Bean
    CommandLineRunner init(UserRepository userRepository){
        return (args) -> {
            // 서버 실행시  무조건 실행.
            userRepository.save(newUser("cristiano", "ronaldo"));
        };
    }
}
