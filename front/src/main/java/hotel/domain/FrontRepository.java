package hotel.domain;

import hotel.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "fronts", path = "fronts")
public interface FrontRepository
    extends PagingAndSortingRepository<Front, Long> {}
