package hotel.infra;

import hotel.domain.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import hotel.external.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
@Transactional
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepository;
    @PostMapping("/reservations/rooms/{id}")
    public ResponseEntity<String> reservationRoom(@PathVariable("id") Long id, @RequestBody ReservationCreated reservationCreated){
        Reservation reservation = new Reservation();

        // LocalDate로 변환
        LocalDate localCheckInDate = reservationCreated.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localCheckOutDate = reservationCreated.getCheckOutDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 두 날짜 사이의 차이 계산
        Long days= ChronoUnit.DAYS.between(localCheckInDate, localCheckOutDate);
        Long payMoney = reservationCreated.getPayMoney() * days;

        reservation.setCustomerId("jhh");
        reservation.setRoomNumber(reservationCreated.getRoomNumber());
        reservation.setCheckInDate(reservationCreated.getCheckInDate());
        reservation.setCheckOutDate(reservationCreated.getCheckOutDate());
        reservation.setRoomId(String.valueOf(id));
        reservation.setRoomType(reservationCreated.getRoomType());
        reservation.setTotalAmount(payMoney);
        reservation.setStatus("예약 신청");
        reservationRepository.save(reservation);


        return ResponseEntity.ok("예약 신청");
    }


    @GetMapping("/reservations/{roomNumber}")
    public ResponseEntity<Reservation> findByRoomNumber(@PathVariable("roomNumber") String roomNumber){
        Optional<Reservation> optionalReservation = reservationRepository.findByRoomNumber(roomNumber);
        if(optionalReservation.isPresent()){
            Reservation reservation = optionalReservation.get();
            return ResponseEntity.ok(reservation);
        }
        return null;
    }

    @GetMapping("/reservations/mypage")
    public ResponseEntity<List<Reservation>> findByCustomerId(String customerId) {
        customerId = "jhh";
        List<Reservation> reservations = reservationRepository.findByCustomerId(customerId);
        return ResponseEntity.ok(reservations);
    }

    @DeleteMapping("/reservations/{id}/cancel")
    public ResponseEntity<String> cancelReservation(@PathVariable("id") Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservationRepository.delete(reservation);
            return ResponseEntity.ok("삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
