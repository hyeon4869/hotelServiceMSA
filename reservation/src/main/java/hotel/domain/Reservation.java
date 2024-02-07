package hotel.domain;

import hotel.ReservationApplication;
import hotel.domain.ReservationCancelled;
import hotel.domain.ReservationCreated;
import hotel.domain.StateUpdated;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import hotel.external.Room;
import hotel.external.RoomService;
import lombok.Data;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "Reservation_table")
@Service
@Data
//<<< DDD / Aggregate Root
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String roomNumber;

    private String customerId;

    private String status;

    private Long totalAmount;

    private Date checkInDate;

    private Date checkOutDate;

    private String roomType;

    private String roomId;
    private String reservationYn;
    @PrePersist
    public void onPrePersist() {

        Room room=ReservationApplication.applicationContext.getBean(RoomService.class).getRoom(Long.valueOf(getRoomId()));
        System.out.println("roomId = "+ getRoomId());
        if (room != null && "empty".equals(room.getStatus())) {
            setReservationYn("Y");
        } else {
           throw new IllegalArgumentException("이미 예약된 방입니다.");
        }
    }

    @PostPersist
    public void onPostPersist() {
        if ("Y".equals(getReservationYn())) {
            ReservationCreated reservationCreated = new ReservationCreated(this);
            reservationCreated.publishAfterCommit();
        }

//        StateUpdated stateUpdated = new StateUpdated(this);
//        stateUpdated.publishAfterCommit();
//
//        ReservationCancelled reservationCancelled = new ReservationCancelled(
//            this
//        );
//        reservationCancelled.publishAfterCommit();
        // Get request from Reservation
        //hotel.external.Reservation reservation =
        //    Application.applicationContext.getBean(hotel.external.ReservationService.class)
        //    .getReservation(/** mapping value needed */);

    }

    @PreRemove
    public void onPreRemove() {
        ReservationCancelled reservationCancelled = new ReservationCancelled(
                this
        );
        reservationCancelled.publishAfterCommit();

    }

    public static ReservationRepository repository() {
        ReservationRepository reservationRepository = ReservationApplication.applicationContext.getBean(
            ReservationRepository.class
        );
        return reservationRepository;
    }

    //<<< Clean Arch / Port Method
    public static void updateState(ReservationCompleted reservationCompleted) {
        //implement business logic here:

             repository().findByRoomId(String.valueOf(reservationCompleted.getId())).ifPresent(reservation->{
            
            reservation.setStatus("예약 완료"); // do something
            repository().save(reservation);

            StateUpdated stateUpdated = new StateUpdated(reservation);
            stateUpdated.publishAfterCommit();

         });


    }
    //>>> Clean Arch / Port Method

    public static void updateState(Paid paid) {
        //implement business logic here:

        /** Example 2:  finding and process*/

         repository().findById(Long.valueOf(paid.getRoomId())).ifPresent(reservation->{

         reservation.setTotalAmount(paid.getPrice()); // do something
         repository().save(reservation);

         StateUpdated stateUpdated = new StateUpdated(reservation);
         stateUpdated.publishAfterCommit();

         });


    }

}
//>>> DDD / Aggregate Root
