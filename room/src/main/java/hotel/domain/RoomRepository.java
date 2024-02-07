package hotel.domain;

import hotel.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "rooms", path = "rooms")
public interface RoomRepository    extends PagingAndSortingRepository<Room, Long> {}
