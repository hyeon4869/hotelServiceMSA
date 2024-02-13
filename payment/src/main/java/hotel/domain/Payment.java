package hotel.domain;

import hotel.PaymentApplication;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import hotel.external.Room;
import hotel.external.RoomService;
import lombok.Data;

@Entity
@Table(name = "Payment_table")
@Data
//<<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String reservationId;

    private Long price;

    private String status;

    private String roomNumber;

    private String customerId;

    private Date checkInDate;

    private Date checkOutDate;

    private String roomType;

    private String roomId;

    private String impuid;
    private String payStatus;


    @PostPersist
    public void onPostPersist() {}

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
            PaymentRepository.class
        );
        return paymentRepository;
    }

    //<<< Clean Arch / Port Method
    public void paySuccess(PaySuccessCommand paySuccessCommand) {


        //implement business logic here:
        Paid paid = new Paid(this);
        setPrice(paid.getPrice());
        setStatus("결제 완료");

        paid.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void payCancel(PayCancelCommand payCancelCommand) {
        //implement business logic here:


        PayCancelled payCancelled = new PayCancelled(this);
        setStatus("결제 취소");
        setPrice(payCancelled.getPrice());
        payCancelled.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void pay(ReservationCreated reservationCreated) {
        //implement business logic here:

        /** Example 1:  new item*/
        Payment payment = new Payment();
        payment.setId(reservationCreated.getId());
        payment.setPrice(reservationCreated.getTotalAmount());
        payment.setCustomerId(reservationCreated.getCustomerId());
        payment.setReservationId(String.valueOf(reservationCreated.getId()));
        payment.setStatus("결제 요청");
        payment.setRoomId(reservationCreated.getRoomId());
        payment.setCustomerId(reservationCreated.getCustomerId());
        payment.setRoomNumber(reservationCreated.getRoomNumber());
        payment.setRoomType(reservationCreated.getRoomType());
        payment.setCheckInDate(reservationCreated.getCheckInDate());
        payment.setCheckOutDate(reservationCreated.getCheckOutDate());
        repository().save(payment);

        /** Example 2:  finding and process
        
        repository().findById(reservationCreated.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }
    //>>> Clean Arch / Port Method

    public static void payReject(ReservationCancelled reservationCancelled) {
        //implement business logic here:

        /** Example 2:  finding and process */

         repository().findById(Long.valueOf(reservationCancelled.getRoomId())).ifPresent(payment->{

         payment.setStatus("결제 취소 요청"); // do something
         repository().save(payment);


         });


    }

}
//>>> DDD / Aggregate Root
