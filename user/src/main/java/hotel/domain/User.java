package hotel.domain;

import hotel.UserApplication;
import hotel.domain.LoggedIn;
import hotel.domain.SignedUp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "User_table")
@Data
// <<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String roles;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    @PostPersist
    public void onPostPersist() {
        SignedUp signedUp = new SignedUp(this);
        signedUp.publishAfterCommit();

        LoggedIn loggedIn = new LoggedIn(this);
        loggedIn.publishAfterCommit();
    }

    public static UserRepository repository() {
        UserRepository userRepository = UserApplication.applicationContext.getBean(
                UserRepository.class);
        return userRepository;
    }

    public void register(SignedUp signedUp) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashPassword = passwordEncoder.encode(signedUp.getPassword());
        setUsername(signedUp.getUsername());
        setPassword(hashPassword);
        setName(signedUp.getName());
        setRoles("ROLE_USER");
        setName(signedUp.getName());
    }


}
// >>> DDD / Aggregate Root
