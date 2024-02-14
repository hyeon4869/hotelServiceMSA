package hotel.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import hotel.auth.JwtAuthenticationFilter;
import hotel.auth.PrincipalDetails;
import hotel.domain.SignedUp;
import hotel.domain.User;
import hotel.domain.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/users")
@Transactional
@Data
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody SignedUp signedUp) {

        User user = new User();
        user.register(signedUp);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{username}/refreshToken")
    public ResponseEntity<String> getRefresh(@PathVariable String username) {
        User user = userRepository.findByUsername(username).get();
        String token = user.getRefreshToken();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/users/token/refresh")
    public ResponseEntity<String> createToken(@RequestBody String refreshToken) {
        Optional<User> optionalUser = userRepository.findByRefreshToken(refreshToken);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 사용자 인증 정보 클래스 객체 생성
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            // accessToken 발급
            String newAccessToken = JwtAuthenticationFilter.createAccessToken(principalDetails);
            return ResponseEntity.ok(newAccessToken);
        } else {
            return ResponseEntity.ok("refresh Token 불일치");
        }

    }

    @PostMapping("/users/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");//Authorization 헤더를 불러온 후 Bearer 부분은 공백으로 처리 
        //이렇게 하면 token 부분만 남게됨
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getClaim("username").asString();
        Optional<User> opUser = userRepository.findByUsername(username);
        if(opUser.isPresent()){
            User user = opUser.get();
            user.setRefreshToken(null);
            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.ok("일치하지 않는 토큰");
        }
    }
}
// >>> Clean Arch / Inbound Adaptor
