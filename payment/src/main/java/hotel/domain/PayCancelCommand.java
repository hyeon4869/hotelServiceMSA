package hotel.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class PayCancelCommand {

    private String roomId;
}
