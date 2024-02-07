package hotel.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

    private final String jwtSecret = JwtProperties.SECRET; // 토큰 생성 시 사용한 시크릿 키

    public AuthorizationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 헤더에서 토큰 추출
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");
            System.out.println(
                    "======================================================================================" + token);
            // 토큰을 이용한 인증 및 권한 부여 로직 구현
            if (isValidToken(token)) {
                // 토큰이 유효하면 다음 단계로 진행
                return chain.filter(exchange);
            } else {
                // 유효하지 않은 토큰이면 인증 실패로 처리

                System.out
                        .println("===============================================================================인증안됨");
                System.out
                        .println("===============================================================================인증안됨");
                System.out
                        .println("===============================================================================인증안됨");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private boolean isValidToken(String token) {
        // 토큰이 null이거나 Bearer로 시작하지 않으면 유효하지 않다고 판단
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        try {
            // "Bearer " 제거
            token = token.substring(7);

            // 토큰 검증
            Map<String, Claim> claims = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                    .build()
                    .verify(token)
                    .getClaims();

            System.out.println("==============================================================================="+
                    claims);
            System.out.println("===============================================================================인증됨");
            System.out.println("===============================================================================인증됨");
            System.out.println("===============================================================================인증됨");

            
            // 여기까지 오류 없이 진행되면 토큰 유효
            return true;
        } catch (Exception e) {
            // 토큰 파싱이나 검증에서 오류 발생 -> 토큰 무효
            return false;
        }
    }

    public static class Config {
        // 필요한 경우 구성 옵션을 추가할 수 있습니다.
    }
}
