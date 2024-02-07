package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ReservationCreated extends AbstractEvent {

    private Long id;
    private String roomNumber;
    private String customerId;
    private String status;
    private Long totalAmount;
    private Date checkInDate;
    private Date checkOutDate;
    private String roomType;
    private String roomId;
    private Long payMoney;

    public ReservationCreated(Reservation aggregate) {
        super(aggregate);
    }

    public ReservationCreated() {
        super();
    }
}
//>>> DDD / Domain Event
