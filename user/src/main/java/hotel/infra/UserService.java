package hotel.infra;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import hotel.auth.JwtAuthenticationFilter;
import hotel.auth.PrincipalDetails;
import hotel.domain.SignedUp;
import hotel.domain.User;
import hotel.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // 회원가입 로직
    public User save(SignedUp signedUp) {
        logger.info("회원가입 시작: ", signedUp.getUsername());

        validateDuplicate(signedUp.getUsername());
        try {
            String hashpassword = passwordEncoder.encode(signedUp.getPassword());
            User user = new User();
            user.register(signedUp);
            user.setPassword(hashpassword);
            userRepository.save(user);
            logger.info("회원가입 완료:", user.getUsername());
            return user;
        } catch (Exception e) {
            logger.error("회원가입 오류 발생", e);
            throw new RuntimeException("서버 내부 오류 발생");
        }
    }

    public void validateDuplicate(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            logger.error("이미 존재하는 아이디로 회원가입 시도", username);
            throw new IllegalArgumentException("이미 존재하는 아이디 입니다.");
        }
    }

    public String getRefreshToken(String username) {
        logger.info("리프레시 토큰 조회 시작: ", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("사용자를 찾을 수 없습니다: ", username);
                    return new NoSuchElementException("사용자를 찾을 수 없습니다.");
                });
        String token = user.getRefreshToken();
        logger.info("리프레시 토큰 조회 완료: ", username);
        return token;
    }

    public String createToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> {
                    logger.error("토큰을 찾을 수 없습니다: ", refreshToken);
                    return new IllegalArgumentException("유효하지 않은 Token입니다.");
                });
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        String newAccessToken = JwtAuthenticationFilter.createAccessToken(principalDetails);
        return newAccessToken;
    }

    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String username = decodedJWT.getClaim("username").asString();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        logger.info("로그아웃 관련 오류가 발생");
                        return new NoSuchElementException("사용자를 찾을 수 없습니다.");
                    });
            user.setRefreshToken(null);
            userRepository.save(user);
        } catch (JWTDecodeException e) {
            logger.error("로그아웃에 사용될 토큰이 존재하지 않아 디코딩에 실패했습니다.");
            throw new JWTDecodeException("로그아웃 상태입니다.");
        }
    }

}
