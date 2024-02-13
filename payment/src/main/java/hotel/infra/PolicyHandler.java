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
    PaymentRepository paymentRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationCreated'"
    )
    public void wheneverReservationCreated_Pay(
        @Payload ReservationCreated reservationCreated
    ) {
        ReservationCreated event = reservationCreated;
        System.out.println(
            "\n\n##### listener Pay : " + reservationCreated + "\n\n"
        );

        // Sample Logic //
        Payment.pay(event);
    }


    @StreamListener(
            value = KafkaProcessor.INPUT,
            condition = "headers['type']=='ReservationCancelled'"
    )
    public void wheneverReservationCancelled_Pay(
            @Payload ReservationCancelled reservationCancelled
    ) {
        ReservationCancelled event = reservationCancelled;
        System.out.println(
                "\n\n##### listener Pay : " + reservationCancelled + "\n\n"
        );

        // Sample Logic //
        Payment.payReject(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
