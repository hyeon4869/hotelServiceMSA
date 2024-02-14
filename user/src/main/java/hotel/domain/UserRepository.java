package hotel.domain;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository
        extends PagingAndSortingRepository<User, Long> {

    @Cacheable(value = "user", key = "#username")
    Optional<User> findByUsername(String username);

    @Cacheable(value = "user", key = "#username")
    Optional<User> findByRefreshToken(String refreshToken);
}
