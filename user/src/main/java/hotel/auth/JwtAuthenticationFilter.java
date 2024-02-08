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

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
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
        String accessToken = JWT.create()
                .withSubject(JwtProperties.TOKENNAME)// 토큰 이름
                .withExpiresAt(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES))) // 토큰 만료 시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("roles", principalDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.trim())
                        .collect(Collectors.toList())) // 권한 정보 추가
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        // refresh 토큰 발급
        String refreshToken = JWT.create()
                .withSubject(JwtProperties.TOKENNAME) // 리프레시 토큰 이름
                .withExpiresAt(Date.from(Instant.now().plus(10080, ChronoUnit.MINUTES))) // 리프레시 토큰 만료 시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        // 리프레시 토큰으로 사용자 업데이트
        try {
            if (principalDetails != null && principalDetails.getUser() != null && userRepository != null) {
                Optional<User> optionalUser = userRepository.findByUsername(principalDetails.getUser().getUsername());
                optionalUser.ifPresent(user -> {
                    user.setRefreshToken(refreshToken);
                    userRepository.save(user);
                });
            } else {
                // userRepository 또는 principalDetails가 null인 경우 로그 출력
                System.out.println("userRepository or principalDetails is null");
            }
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }

        // 헤더에 토큰 추가
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        response.setHeader("Access-Control-Expose-Headers",
                JwtProperties.HEADER_STRING);

        // 유저네임, 패스워드가 정상이면 jwt 토큰을 생성하고
        // 클라이언트 쪽으로 JWT토큰을 응답
        // 요청할 때마다 JWT토큰을 가지고 요청하며
        // 서버는 JWT토큰이 유효한지를 판단(필터를 만들어야함)
        System.out.println(accessToken);
        System.out.println("리프레쉬 토큰 ===========================================" + refreshToken);
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
