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
    ReservationRepository reservationRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationCompleted'"
    )
    public void wheneverReservationCompleted_UpdateState(
        @Payload ReservationCompleted reservationCompleted
    ) {
        ReservationCompleted event = reservationCompleted;
        System.out.println(
            "\n\n##### listener UpdateState : " + reservationCompleted + "\n\n"
        );

        // Sample Logic //
        Reservation.updateState(event);
    }
    @StreamListener(
            value = KafkaProcessor.INPUT,
            condition = "headers['type']=='Paid'"
    )
    public void wheneverPaid_UpdateState(@Payload Paid paid) {
        Paid event = paid;
        System.out.println("\n\n##### listener UpdateState : " + paid + "\n\n");

        // Sample Logic //
        Reservation.updateState(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
