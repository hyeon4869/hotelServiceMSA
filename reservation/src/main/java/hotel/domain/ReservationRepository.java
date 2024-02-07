package hotel.domain;

import hotel.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "reservations",
    path = "reservations"
)
public interface ReservationRepository
    extends PagingAndSortingRepository<Reservation, Long> {
    Optional<Reservation> findByRoomId(String roomId);

    Optional<Reservation> findByRoomNumber(String roomNumber);

    List<Reservation> findByCustomerId(String jhh);
}
