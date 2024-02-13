package hotel.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

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
                String payload = token.split("\\.")[1]; // .을 기준으로 페이로드를 추출
                String json = new String(Base64.getDecoder().decode(payload));// 페이로드는 base64로 인코딩 되어 있어서 base64로 디코딩
                String username = new JSONObject(json).getString("username");// json형태로 저장되어 있기에 json으로 추출

                // DecodedJWT decodedJWT = JWT.decode(token); 
                // String username = decodedJWT.getClaim("username").asString();

                //decodedJWT로 가져오는 값들
                // 토큰의 헤더 가져오기
                // String header = decodedJWT.getHeader();
                // 토큰의 페이로드 가져오기
                // String payload = decodedJWT.getPayload();
                // username claim의 값 가져오기
                // String username = decodedJWT.getClaim("username").asString();

                String refreshToken = getRefreshToken(username);
                String newToken = requestNewToken(refreshToken);

                exchange.getResponse().getHeaders().set("authorization", "Bearer " + newToken);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isTokenExpired(String token) {
        try {
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)// 검증
                    .getClaims();// 검증된 토큰의 claims를 가져옴
            System.out.println("토큰이 유효함 ");
            return false;
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    // 리프레쉬 토큰 알아내기 위한 것
    private String getRefreshToken(String username) {
        WebClient webClient = WebClient.create("http://localhost:8088");// 요청 주소
        String refreshToken = webClient.get()
                .uri("/users/{username}/refreshToken", username) // 상세 주소, 어느 사용자의 정보를 얻는 것인지 알아야하기 떄문에 {username} 필요
                .retrieve()// 결과 받기
                .bodyToMono(String.class)// 비동기적인 응답을 위한 것, 본문을 mono로 변환하고 이를 통해 비동기적으로 데이터를 가져옴
                // 모노의 타입을 String으로 지정
                // String으로 지정하지 않으면 타입이 맞지 않아서 컴파일 오류
                .block();// 비동기적인 작업이 완료될 때 까지 기다림

        return refreshToken;
    }

    // 새로운 엑세스 토큰 발급을 위한 것
    private String requestNewToken(String refreshToken) {
        WebClient webClient = WebClient.create("http://localhost:8088");
        String newToken = webClient.post()
                .uri("/users/token/refresh") // 리프레시 토큰에 사용자의 정보가 담겨있으니 상관없음
                .body(BodyInserters.fromValue(refreshToken))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println("재발급 실행");
        System.out.println(newToken);
        return newToken;
    }
    // 리액티브 프로그래밍 라이브러리의 핵심 타입 중 하나입니다
    // Mono: Mono는 0 또는 1개의 항목을 가지는 리액티브 스트림입니다. 즉, 단일 값을 표현합니다.
    // Mono는 비동기적인 작업의 결과를 나타낼 때 주로 사용됩니다.
    // 예를 들어, 파일에서 데이터를 읽거나 원격 서비스에서 데이터를 가져오는 등의 작업에서 Mono를 사용할 수 있습니다.

//     효율성: 비동기 프로그래밍을 사용하면, 오래 걸리는 작업을 기다리는 동안 다른 작업을 수행할 수 있습니다. 이로 인해 프로그램의 효율성을 높일 수 있습니다.
// 응답성: 비동기 프로그래밍을 사용하면, 오래 걸리는 작업을 처리하는 동안도 사용자 인터페이스와 같은 것을 계속해서 업데이트할 수 있습니다. 이로 인해 프로그램의 응답성을 높일 수 있습니다.
// 병렬성: 비동기 프로그래밍을 사용하면, 여러 작업을 동시에 병렬로 처리할 수 있습니다. 이로 인해 프로그램의 성능을 높일 수 있습니다.

    public static class Config {
    }
}