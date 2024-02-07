package hotel.infra;

import hotel.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class FrontHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Front>> {

    @Override
    public EntityModel<Front> process(EntityModel<Front> model) {
        return model;
    }
}
