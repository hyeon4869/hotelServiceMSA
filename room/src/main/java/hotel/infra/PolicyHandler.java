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
    RoomRepository roomRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PayUpdated'"
    )
    public void wheneverPayUpdated_ReservationComplete(
        @Payload PayUpdated payUpdated
    ) {
        PayUpdated event = payUpdated;
        System.out.println(
            "\n\n##### listener ReservationComplete : " + payUpdated + "\n\n"
        );

        // Sample Logic //
        Room.reservationComplete(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='FrontCancelled'"
    )
    public void wheneverFrontCancelled_ReservationCancel(
        @Payload FrontCancelled frontCancelled
    ) {
        FrontCancelled event = frontCancelled;
        System.out.println(
            "\n\n##### listener ReservationCancel : " + frontCancelled + "\n\n"
        );

        // Sample Logic //
        Room.reservationCancel(event);
    }
    @StreamListener(
            value = KafkaProcessor.INPUT,
            condition = "headers['type']=='ReservationCreated'"
    )
    public void wheneverReservationCreated_RoomUpdateState(
            @Payload ReservationCreated reservationCreated
    ) {
        ReservationCreated event = reservationCreated;
        System.out.println(
                "\n\n##### listener RoomUpdateState : " +
                        reservationCreated +
                        "\n\n"
        );

        // Sample Logic //
        Room.roomUpdateState(event);
    }
}

//>>> Clean Arch / Inbound Adaptor
