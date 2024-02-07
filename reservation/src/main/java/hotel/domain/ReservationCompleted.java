package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class ReservationCompleted extends AbstractEvent {

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
}
