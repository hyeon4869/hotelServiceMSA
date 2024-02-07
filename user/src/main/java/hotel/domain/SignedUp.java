package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class SignedUp extends AbstractEvent {

    private Long id;
    private String username;
    private String password;
    private String name;
    private String roles;

    public SignedUp(User aggregate) {
        super(aggregate);
    }

    public SignedUp() {
        super();
    }
}
//>>> DDD / Domain Event
