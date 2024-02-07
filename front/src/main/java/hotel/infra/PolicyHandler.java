package hotel.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotel.config.kafka.KafkaProcessor;
import hotel.domain.*;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    FrontRepository frontRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationCreated'"
    )
    public void wheneverReservationCreated_ReservationAccept(
        @Payload ReservationCreated reservationCreated
    ) {
        ReservationCreated event = reservationCreated;
        System.out.println(
            "\n\n##### listener ReservationAccept : " +
            reservationCreated +
            "\n\n"
        );

        // Sample Logic //
        Front.reservationAccept(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PayCancelled'"
    )
    public void wheneverPayCancelled_ReservationCancel(
        @Payload PayCancelled payCancelled
    ) {
        PayCancelled event = payCancelled;
        System.out.println(
            "\n\n##### listener ReservationCancel : " + payCancelled + "\n\n"
        );

        // Sample Logic //
        Front.reservationCancel(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Paid'"
    )
    public void wheneverPaid_PayUpdate(@Payload Paid paid) {
        Paid event = paid;
        System.out.println("\n\n##### listener PayUpdate : " + paid + "\n\n");

        // Sample Logic //
        Front.payUpdate(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
