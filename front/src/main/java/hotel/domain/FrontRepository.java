package hotel.domain;

import hotel.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "fronts", path = "fronts")
public interface FrontRepository
    extends PagingAndSortingRepository<Front, Long> {
    Optional<Front> findByReservationId(String reservationId);

    Optional<Front> findByRoomId(String roomId);
}
