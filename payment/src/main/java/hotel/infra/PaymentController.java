package hotel.infra;

import hotel.domain.*;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/payments")
@Transactional
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @RequestMapping(value = "payments/{id}/paysuccess", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public Payment paySuccess(
            @PathVariable(value = "id") String reservationId,
            @RequestBody PaySuccessCommand paySuccessCommand,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        System.out.println("##### /payment/paySuccess  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findByReservationId(reservationId);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.paySuccess(paySuccessCommand);

        paymentRepository.save(payment);
        return payment;
    }

    @RequestMapping(value = "payments/{id}/paycancel", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public Payment payCancel(
            @PathVariable(value = "id") String reservationId,
            @RequestBody PayCancelCommand payCancelCommand,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        System.out.println("##### /payment/payCancel  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findByReservationId(reservationId);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.payCancel(payCancelCommand);

        paymentRepository.save(payment);
        return payment;
    }

    @ResponseBody
    @RequestMapping("/verify/imp_uid")
    public String requestMethodName(@RequestParam String param) {
        return new String();
    }
    
    // @GetMapping("/payments/{id}")
    // public ResponseEntity<Payment> findByReservationId(@PathVariable("id") String
    // Id){
    // Optional<Payment> optionalPayment = paymentRepository.findByRoomId(roomId);
    // if(optionalPayment.isPresent()){
    // Payment payment = optionalPayment.get();
    // return ResponseEntity.ok(payment);
    // }
    // return null;
    // }
}
// >>> Clean Arch / Inbound Adaptor
