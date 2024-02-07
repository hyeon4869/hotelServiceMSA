package hotel.domain;

import hotel.PaymentApplication;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
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

    @PostPersist
    public void onPostPersist() {
        // Get request from Payment
        //hotel.external.Payment payment =
        //    Application.applicationContext.getBean(hotel.external.PaymentService.class)
        //    .getPayment(/** mapping value needed */);

        // Get request from Payment
        //hotel.external.Payment payment =
        //    Application.applicationContext.getBean(hotel.external.PaymentService.class)
        //    .getPayment(/** mapping value needed */);

    }

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
        paid.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void payCancel(PayCancelCommand payCancelCommand) {
        //implement business logic here:

        PayCancelled payCancelled = new PayCancelled(this);
        payCancelled.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    public static void pay(ReservationCreated reservationCreated) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        */

        /** Example 2:  finding and process
        
        repository().findById(reservationCreated.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void payReject(ReservationCancelled reservationCancelled) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        */

        /** Example 2:  finding and process
        
        repository().findById(reservationCancelled.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
