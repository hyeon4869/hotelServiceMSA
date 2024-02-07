package hotel.infra;

import hotel.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class PaymentHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Payment>> {

    @Override
    public EntityModel<Payment> process(EntityModel<Payment> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/paysuccess")
                .withRel("paysuccess")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/paycancel")
                .withRel("paycancel")
        );

        return model;
    }
}
