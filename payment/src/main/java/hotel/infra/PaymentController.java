package hotel.infra;

import hotel.domain.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/payments")
@Transactional
public class PaymentController {

    @Autowired
    PaymentRepository paymentRepository;

    @RequestMapping(
        value = "payments/{id}/paysuccess",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment paySuccess(
        @PathVariable(value = "id") Long id,
        @RequestBody PaySuccessCommand paySuccessCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /payment/paySuccess  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.paySuccess(paySuccessCommand);

        paymentRepository.save(payment);
        return payment;
    }

    @RequestMapping(
        value = "payments/{id}/paycancel",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Payment payCancel(
        @PathVariable(value = "id") Long id,
        @RequestBody PayCancelCommand payCancelCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /payment/payCancel  called #####");
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        optionalPayment.orElseThrow(() -> new Exception("No Entity Found"));
        Payment payment = optionalPayment.get();
        payment.payCancel(payCancelCommand);

        paymentRepository.save(payment);
        return payment;
    }
}
//>>> Clean Arch / Inbound Adaptor
