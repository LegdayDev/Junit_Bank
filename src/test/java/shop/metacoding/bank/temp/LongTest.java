package shop.metacoding.bank.temp;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LongTest {

    @Test
    public void long_test3() throws Exception {
        //given
        Long v1 = 129L;
        Long v2 = 129L;
        //when

        //then
        assertThat(v1).isNotSameAs(v2);
    }

    @Test
    public void long_test2() throws Exception {
        //given
        // Long 타입은 8Bit(-128 ~ 127) 범위 까지는 비교가능하지만 그 이상은 제대로 비교가 안된다.
        Long v1 = 128L;
        Long v2 = 128L;

        //when
        if(v1 == v2){
            System.out.println("같습니다!");
        }else{
            System.out.println("같지 않습니다!");
        }
        //then
    }

    @Test
    public void long_test() throws Exception {
        //given
        Long number1 = 1111L;
        Long number2 = 1111L;

        //when
        if (number1.longValue() != number2.longValue()) {
            System.out.println("동일하지 않습니다.");
        } else {
            System.out.println("동일합니다.");
        }

        Long amount1 = 1000L;
        Long amount2 = 1000L;

        if(amount1 == amount2){
            System.out.println("같습니다.");
        }else{
            System.out.println("다릅니다.");
        }

        //then
    }
}
