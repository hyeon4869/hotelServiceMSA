package hotel.external;

import lombok.Data;

import java.util.Date;

@Data
public class Room {

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
