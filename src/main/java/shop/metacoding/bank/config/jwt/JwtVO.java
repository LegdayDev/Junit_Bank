package shop.metacoding.bank.config.jwt;

/**
 * SECRET : 절대 노출 되면 안된다.(클라우드 AWS - 환경변수, 파일)
 * 리플래시 토큰(에세스 토큰이 만료됐을 때 자동생성해주는 토큰 -> 자동 로그인)
 */
public interface JwtVO {
    public static final String SECRET = "메타코딩"; // HS256 알고리즘을 위한 서버만 알고있는 키 값
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 만료시간(ms)
    public static final String TOKEN_PREFIX = "Bearer "; // Protocol 이다
    public static final String HEADER = "Authorization";
}
