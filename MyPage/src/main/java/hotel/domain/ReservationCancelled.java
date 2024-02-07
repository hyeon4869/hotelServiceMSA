package hotel.domain;

import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class ReservationCancelled extends AbstractEvent {

    private Long id;
    private String roomNumber;
    private String customerId;
    private String status;
    private Long totalAmount;
    private Date checkInDate;
    private Date checkOutDate;
    private String roomType;
    private String roomId;
}
