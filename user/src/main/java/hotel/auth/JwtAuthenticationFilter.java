package hotel.auth;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import hotel.domain.User;
import hotel.domain.UserRepository;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {//생성자, 필터는 bean이 아니기 때문에 외부 bean을 사용하려면 생성자 주입으로 사용
        this.authenticationManager = authenticationManager;
        this.userRepository=userRepository;
        // 로그인 주소를 변경합니다.
        this.setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter :로그인 시도중");
        try {
            ObjectMapper om = new ObjectMapper();// 객체 매핑
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        System.out.println("인증이 무사히 성공됨");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

     // access 토큰 발급
        String accessToken = createAccessToken(principalDetails);

        // refresh 토큰 발급
        String refreshToken = JWT.create()
            .withSubject(JwtProperties.TOKENNAME)
            .withExpiresAt(Date.from(Instant.now().plus(10080, ChronoUnit.MINUTES)))
            .withClaim("id", principalDetails.getUser().getId())
            .withClaim("username", principalDetails.getUser().getUsername())
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        // 리프레시 토큰으로 사용자 업데이트
        Optional<User> optionalUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
        optionalUser.ifPresent(user -> {
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        });

        // 헤더에 토큰 추가
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.setHeader("Access-Control-Expose-Headers", JwtProperties.HEADER_STRING);
    }

    // access 토큰 생성 메소드
    public static String createAccessToken(PrincipalDetails principalDetails) {
        String accessToken = JWT.create()
            .withSubject(JwtProperties.TOKENNAME)
            .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
            .withClaim("id", principalDetails.getUser().getId())
            .withClaim("username", principalDetails.getUser().getUsername())
            .withClaim("roles", principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::trim)
                .collect(Collectors.toList()))
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));
            
        return accessToken;
    }
}
// setHeader를 사용하면
// 기존에 설정된 Access-Control-Expose-Headers 헤더를 덮어쓰게 되므로,
// 이 헤더가 CORS 정책에 의해 브라우저에서 제대로 처리되고,
// 따라서 JWT 토큰을 포함한 헤더를 클라이언트에서 볼 수 있게 됩니다.

// 반면, addHeader를 사용하면 기존에 설정된 Access-Control-Expose-Headers 헤더에 새로운 값을 추가하게
// 되므로,
// 이 헤더가 CORS 정책에 의해 브라우저에서 제대로 처리되지 않을 수 있습니다.

// 이로 인해 JWT 토큰을 포함한 헤더가 클라이언트에서 보이지 않게 되는 것으로 보입니다.
