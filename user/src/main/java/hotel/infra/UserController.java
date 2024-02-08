package hotel.infra;



import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<User> register(@RequestBody SignedUp signedUp){


        User user = new User();
        user.register(signedUp);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{username}/refreshToken")
    public ResponseEntity<String> getRefresh(@PathVariable String username){
        User user = userRepository.findByUsername(username).get();
        String token = user.getRefreshToken();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/users/token/refresh")
    public ResponseEntity<String> createToken(@RequestBody String refreshToken){
        Optional<User> optionalUser = userRepository.findByRefreshToken(refreshToken);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            //사용자 인증 정보 클래스 객체 생성
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            //accessToken 발급
            String newAccessToken = JwtAuthenticationFilter.createAccessToken(principalDetails);
            return ResponseEntity.ok(newAccessToken);
        } else{
            return ResponseEntity.ok("refresh Token 불일치");
        }
        
    }
}
//>>> Clean Arch / Inbound Adaptor
