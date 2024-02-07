package hotel.domain;

import hotel.FrontApplication;
import hotel.domain.FrontCancelled;
import hotel.domain.PayUpdated;
import hotel.domain.ReservationAccepted;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Front_table")
@Data
//<<< DDD / Aggregate Root
public class Front {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String roomNumber;

    private String customerId;

    private Date checkInDate;

    private Date checkOutDate;

    private String roomType;

    private String status;

    private String reservationId;

    private String roomId;

    @PostPersist
    public void onPostPersist() {
        ReservationAccepted reservationAccepted = new ReservationAccepted(this);
        reservationAccepted.publishAfterCommit();

        FrontCancelled frontCancelled = new FrontCancelled(this);
        frontCancelled.publishAfterCommit();

        PayUpdated payUpdated = new PayUpdated(this);
        payUpdated.publishAfterCommit();
    }

    public static FrontRepository repository() {
        FrontRepository frontRepository = FrontApplication.applicationContext.getBean(
            FrontRepository.class
        );
        return frontRepository;
    }

    //<<< Clean Arch / Port Method
    public static void reservationAccept(
        ReservationCreated reservationCreated
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Front front = new Front();
        repository().save(front);

        ReservationAccepted reservationAccepted = new ReservationAccepted(front);
        reservationAccepted.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(reservationCreated.get???()).ifPresent(front->{
            
            front // do something
            repository().save(front);

            ReservationAccepted reservationAccepted = new ReservationAccepted(front);
            reservationAccepted.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void reservationCancel(PayCancelled payCancelled) {
        //implement business logic here:

        /** Example 1:  new item 
        Front front = new Front();
        repository().save(front);

        FrontCancelled frontCancelled = new FrontCancelled(front);
        frontCancelled.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(payCancelled.get???()).ifPresent(front->{
            
            front // do something
            repository().save(front);

            FrontCancelled frontCancelled = new FrontCancelled(front);
            frontCancelled.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void payUpdate(Paid paid) {
        //implement business logic here:

        /** Example 1:  new item 
        Front front = new Front();
        repository().save(front);

        PayUpdated payUpdated = new PayUpdated(front);
        payUpdated.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(paid.get???()).ifPresent(front->{
            
            front // do something
            repository().save(front);

            PayUpdated payUpdated = new PayUpdated(front);
            payUpdated.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
