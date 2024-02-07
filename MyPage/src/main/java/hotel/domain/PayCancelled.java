package hotel.domain;

import hotel.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
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
}
