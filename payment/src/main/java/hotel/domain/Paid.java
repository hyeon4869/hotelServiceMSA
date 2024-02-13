package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Paid extends AbstractEvent {

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
    private String impuid;
    private String payStatus;
    public Paid(Payment aggregate) {
        super(aggregate);
    }

    public Paid() {
        super();
    }
}
//>>> DDD / Domain Event
