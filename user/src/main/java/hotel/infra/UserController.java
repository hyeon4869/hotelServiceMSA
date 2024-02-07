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
}
//>>> Clean Arch / Inbound Adaptor
