package shop.metacoding.bank.temp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;

/**
 * <h2>java.util.regex.Pattern 연습용</h2>
 */
public class RegexTest {

    @Test
    @DisplayName("한글만 되는 테스트")
    public void onlyKorean() throws Exception {
        String value = "나";
        boolean result = Pattern.matches("^[가-힣]$", value);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("한글만 안되는 테스트")
    public void noKorean() throws Exception {
        String value = " ~fdf";
        boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*+$", value);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("영어만 되는 테스트")
    public void onlyEnglish() throws Exception {
        String value = "as";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("영어만 안되는 테스트")
    public void noEnglish() throws Exception {
        String value = "ㅇㄹㅇ!!";
        boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("영어와 숫자만 되는 테스트")
    public void onlyEnglishAndNumber() throws Exception {
        String value = "ab12";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("영어만 되고 길이는 2~4 테스트")
    public void onlyEnglishAndLength2_4() throws Exception {
        String value = "aa";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        assertThat(result).isTrue();
    }

    //username, email, fullName 테스트

    @Test
    @DisplayName("usernmae 영어,숫자 2~20자 테스트")
    public void usernameValidation() throws Exception {
        // 영문, 숫자되고 길이는 최소 2~20자 이내
        String username = "ronaldo";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("fullName 영어,한글 1~20자")
    public void fullNameValidation() throws Exception {
        String fullName = "CristianoRonaldo축신축왕";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullName);
        assertThat(result).isTrue();
    }
    
    @Test
    @DisplayName("email 이메일형식")
    public void emailValidation() throws Exception {
        String email = "rddfs@nate.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,6}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", email);
        assertThat(result).isTrue();
    }

}
