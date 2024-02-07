package hotel.domain;

import hotel.domain.*;
import hotel.infra.AbstractEvent;
import java.util.*;
import lombok.*;

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
}
