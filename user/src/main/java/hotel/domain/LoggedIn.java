package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class LoggedIn extends AbstractEvent {

    private Long id;
    private String username;
    private String password;
    private String name;

    public LoggedIn(User aggregate) {
        super(aggregate);
    }

    public LoggedIn() {
        super();
    }
}
//>>> DDD / Domain Event
