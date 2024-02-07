package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RoomReleased extends AbstractEvent {

    private Long id;
    private String customerId;
    private String roomType;
    private String status;
    private String features;
    private Long pricePerNight;
    private String roomNumber;
    private String reservationId;
    private Date checkInDate;
    private Date checkOutDate;

    public RoomReleased(Room aggregate) {
        super(aggregate);
    }

    public RoomReleased() {
        super();
    }
}
//>>> DDD / Domain Event
