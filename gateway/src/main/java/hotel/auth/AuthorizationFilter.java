package hotel.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;

import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
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
                return chain.filter(exchange);
            } else {
                if (token != null) {
                    token = token.substring(7);
                    String payload = token.split("\\.")[1];
                    String json = new String(Base64.getDecoder().decode(payload));
                    String username = new JSONObject(json).getString("username");
                    String refreshToken = getRefreshToken(username);
                    String newToken = requestNewToken(refreshToken);
                    System.out.println("=====================================================리프레시 토큰  "+refreshToken);
                    System.out.println("=====================================================재발급된 엑세스 토큰  "+newToken);
                    

                                    // 응답 헤더에 새 토큰 추가
                exchange.getResponse().getHeaders().add("newauthorization", "Bearer " + newToken);

                }

                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };                
    }

    private boolean isValidToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return false;
        }

        try {
            token = token.substring(7);

            Map<String, Claim> claims = JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                    .build()
                    .verify(token)
                    .getClaims();

            return true;
        } catch (TokenExpiredException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    private String getRefreshToken(String username) {
        WebClient webClient = WebClient.create("http://localhost:8088"); // user의 URL
        String refreshToken = webClient.get() // GET 요청
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{username}/refreshToken")
                        .build(username)) // 사용자 아이디를 이용하여 리프레시 토큰 가져오는 API의 경로
                .retrieve() // 응답 받아오기
                .bodyToMono(String.class) // 응답 본문을 String으로 변환
                .block(); // Mono의 결과를 블로킹 방식으로 받아오기

        return refreshToken;
    }

    private String requestNewToken(String refreshToken) {
        WebClient webClient = WebClient.create("http://localhost:8088"); // User 마이크로서비스의 url
        String newToken = webClient.post() // POST 요청
                .uri("/users/token/refresh") // 토큰 재발급 요청 경로
                .body(BodyInserters.fromValue(refreshToken)) // 요청 본문에 리프레시 토큰 삽입
                .retrieve() // 응답 받아오기
                .bodyToMono(String.class) // 응답 본문을 String으로 변환
                .block(); // Mono의 결과를 블로킹 방식으로 받아오기

        return newToken;
    }

    public static class Config {
        // 필요한 경우 구성 옵션을 추가할 수 있습니다.
    }
}
