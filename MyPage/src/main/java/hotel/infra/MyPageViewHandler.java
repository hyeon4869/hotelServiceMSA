package hotel.infra;

import hotel.config.kafka.KafkaProcessor;
import hotel.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MyPageViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private MyPageRepository myPageRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCreated_then_CREATE_1(
        @Payload ReservationCreated reservationCreated
    ) {
        try {
            if (!reservationCreated.validate()) return;

            // view 객체 생성
            MyPage myPage = new MyPage();
            // view 객체에 이벤트의 Value 를 set 함
            myPage.setId(reservationCreated.getId());
            myPage.setRoomNumber(reservationCreated.getRoomNumber());
            myPage.setCustomerId(reservationCreated.getCustomerId());
            myPage.setStatus("예약 요청");
            myPage.setCheckInDate(reservationCreated.getCheckInDate());
            myPage.setCheckOutDate(reservationCreated.getCheckOutDate());
            myPage.setRoomType(reservationCreated.getRoomType());
            // view 레파지 토리에 save
            myPageRepository.save(myPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaid_then_UPDATE_1(@Payload Paid paid) {
        try {
            if (!paid.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                paid.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("결제");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPayCancelled_then_UPDATE_2(
        @Payload PayCancelled payCancelled
    ) {
        try {
            if (!payCancelled.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                payCancelled.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("결제 취소");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationAccepted_then_UPDATE_3(
        @Payload ReservationAccepted reservationAccepted
    ) {
        try {
            if (!reservationAccepted.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                reservationAccepted.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("예약 수락");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenFrontCancelled_then_UPDATE_4(
        @Payload FrontCancelled frontCancelled
    ) {
        try {
            if (!frontCancelled.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                frontCancelled.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("예약 취소");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCompleted_then_UPDATE_5(
        @Payload ReservationCompleted reservationCompleted
    ) {
        try {
            if (!reservationCompleted.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                reservationCompleted.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("예약");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRoomReleased_then_UPDATE_6(
        @Payload RoomReleased roomReleased
    ) {
        try {
            if (!roomReleased.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                roomReleased.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("빈방");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPayUpdated_then_UPDATE_7(@Payload PayUpdated payUpdated) {
        try {
            if (!payUpdated.validate()) return;
            // view 객체 조회

            List<MyPage> myPageList = myPageRepository.findByStatus(
                payUpdated.getStatus()
            );
            for (MyPage myPage : myPageList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                myPage.setStatus("결제완료");
                // view 레파지 토리에 save
                myPageRepository.save(myPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationCancelled_then_DELETE_1(
        @Payload ReservationCancelled reservationCancelled
    ) {
        try {
            if (!reservationCancelled.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            myPageRepository.deleteByStatus(reservationCancelled.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
