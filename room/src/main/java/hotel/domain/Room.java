package hotel.domain;

import hotel.RoomApplication;
import hotel.domain.ReservationCompleted;
import hotel.domain.RoomReleased;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Entity
@Table(name = "Room_table")
@Service
@Data
//<<< DDD / Aggregate Root
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public static List<Room> findAll() {
        return (List<Room>) repository().findAll();
    }

    @PostPersist
    public void onPostPersist() {
//        ReservationCompleted reservationCompleted = new ReservationCompleted(
//            this
//        );
//        reservationCompleted.publishAfterCommit();
//
//        RoomReleased roomReleased = new RoomReleased(this);
//        roomReleased.publishAfterCommit();
       // RoomCreated roomCreated = new RoomCreated(this);
//        setStatus("empty");
//        setRoomType(roomCreated.getRoomType());
//        setRoomNumber(roomCreated.getRoomNumber());
//        setFeatures(roomCreated.getFeatures());
//        setPricePerNight(roomCreated.getPricePerNight());
     //   roomCreated.publishAfterCommit();

    }

    public static RoomRepository repository() {
        RoomRepository roomRepository = RoomApplication.applicationContext.getBean(
            RoomRepository.class
        );
        return roomRepository;
    }

    //<<< Clean Arch / Port Method
    public static void reservationComplete(PayUpdated payUpdated) {
        //implement business logic here:

        /** Example 1:  new item 
        Room room = new Room();
        repository().save(room);

        ReservationCompleted reservationCompleted = new ReservationCompleted(room);
        reservationCompleted.publishAfterCommit();
        */

        /** Example 2:  finding and process*/

        repository().findById(Long.valueOf(payUpdated.getRoomId())).ifPresent(room->{

            room.setReservationId(payUpdated.getReservationId());
            room.setStatus("예약");// do something
            room.setCustomerId(payUpdated.getCustomerId());
            room.setCheckInDate(payUpdated.getCheckInDate());
            room.setCheckOutDate(payUpdated.getCheckOutDate());
            repository().save(room);

            ReservationCompleted reservationCompleted = new ReservationCompleted(room);
            reservationCompleted.publishAfterCommit();

         });


    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void reservationCancel(FrontCancelled frontCancelled) {
        //implement business logic here:


         repository().findById(Long.valueOf(frontCancelled.getRoomId())).ifPresent(room->{
            
            room.setStatus("empty"); // do something
             room.setReservationId(null);
             room.setCustomerId(null);
             room.setCheckInDate(null);
             room.setCheckOutDate(null);
            repository().save(room);

            RoomReleased roomReleased = new RoomReleased(room);
            roomReleased.publishAfterCommit();

         });


    }
    //>>> Clean Arch / Port Method

    public static void roomUpdateState(ReservationCreated reservationCreated) {
        //implement business logic here:

        /** Example 1:  new item
         Room room = new Room();
         repository().save(room);

         */

        /** Example 2:  finding and process*/

         repository().findById(Long.valueOf(reservationCreated.getRoomId())).ifPresent(room->{

         room.setReservationId(String.valueOf(reservationCreated.getId()));
         room.setCustomerId(reservationCreated.getCustomerId());// do something
         repository().save(room);


         });


    }
    //>>> Clean Arch / Port Method
}
//>>> DDD / Aggregate Root
