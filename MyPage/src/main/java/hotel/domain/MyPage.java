package hotel.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "MyPage_table")
@Data
public class MyPage {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String roomNumber;
    private String customerId;
    private String status;
    private Date checkInDate;
    private Date checkOutDate;
    private String roomType;
}
