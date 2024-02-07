package hotel.auth;

public interface JwtProperties {
    String SECRET = "mcos"; //비밀값
    int EXPRIRATION_TIME = 60000*10;//10분
    String TOKEN_PREFIX = "Bearer ";//접두, 공백을 만들면 헤더에 저장할 수 있고 공백이 없으면 쿠키 
    String HEADER_STRING = "Authorization";
    int TIME=550;// 540이 현재시간 그 이후로 1당 1분
    String TOKENNAME = "COS토큰";
}
