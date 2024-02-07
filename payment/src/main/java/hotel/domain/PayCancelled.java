package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PayCancelled extends AbstractEvent {

    private Long id;
    private String reservationId;
    private Long price;
    private String status;
    private String roomNumber;
    private String customerId;
    private String checkInDate;
    private String checkOutDate;
    private String roomType;
    private String roomId;

    public PayCancelled(Payment aggregate) {
        super(aggregate);
    }

    public PayCancelled() {
        super();
    }
}
//>>> DDD / Domain Event
