package hotel.infra;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import hotel.auth.JwtAuthenticationFilter;
import hotel.auth.PrincipalDetails;
import hotel.domain.*;

@RestController
@Transactional()
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<?> createUser(@RequestBody SignedUp signedUp) {
        User user = userService.save(signedUp);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{username}/refreshToken")
    public ResponseEntity<String> getRefresh(@PathVariable String username) {
        String token=userService.getRefreshToken(username);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/users/token/refresh")
    public ResponseEntity<?> createToken(@RequestBody String refreshToken) {
        String newAccessToken=userService.createToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        logger.info("로그아웃 요청이 들어옴");
        userService.logout(request);
        return ResponseEntity.ok("로그아웃 성공");
    }
}
