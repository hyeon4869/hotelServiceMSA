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

    private final String jwtSecret = JwtProperties.SECRET;

    public AuthorizationFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
    
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
    
            String token = authorizationHeader.substring(7);
    
            if (isTokenExpired(token)) {
                String payload = token.split("\\.")[1];
                String json = new String(Base64.getDecoder().decode(payload));
                String username = new JSONObject(json).getString("username");
                String refreshToken = getRefreshToken(username);
                String newToken = requestNewToken(refreshToken);
    
                exchange.getResponse().getHeaders().add("newauthorization", "Bearer " + newToken);
            }
    
            return chain.filter(exchange);
        };
    }
    
    private boolean isTokenExpired(String token) {
        try {
            token = token.substring(7);

            JWT.require(Algorithm.HMAC512(jwtSecret.getBytes()))
                    .build()
                    .verify(token)//검증 
                    .getClaims();//검증된 토큰의 claims를 가져옴

            return false;
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    //리프레쉬 토큰 알아내기 위한 것
    private String getRefreshToken(String username) {
        WebClient webClient = WebClient.create("http://localhost:8088");//요청 주소
        String refreshToken = webClient.get()
                .uri("/users/{username}/refreshToken", username) //상세 주소, 
                .retrieve()
                .bodyToMono(String.class)//비동기적인 응답을 위한 것, 본문을 mono로 변환하고 이를 통해 비동기적으로 데이터를 가져옴
                //모노의 타입을 String으로 지정
                //String으로 지정하지 않으면 타입이 맞지 않아서 컴파일 오류
                .block();//비동기적인 작업이 완료될 때 까지 기다림
    
        return refreshToken;
    }
    
    //새로운 엑세스 토큰 발급을 위한 것
    private String requestNewToken(String refreshToken) {
        WebClient webClient = WebClient.create("http://localhost:8088");
        String newToken = webClient.post()
                .uri("/users/token/refresh")
                .body(BodyInserters.fromValue(refreshToken))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    
        return newToken;
    }
    
    // 리액티브 프로그래밍 라이브러리의 핵심 타입 중 하나입니다
    //Mono: Mono는 0 또는 1개의 항목을 가지는 리액티브 스트림입니다. 즉, 단일 값을 표현합니다.
    // Mono는 비동기적인 작업의 결과를 나타낼 때 주로 사용됩니다. 
    //예를 들어, 파일에서 데이터를 읽거나 원격 서비스에서 데이터를 가져오는 등의 작업에서 Mono를 사용할 수 있습니다.
    public static class Config {
    }
}