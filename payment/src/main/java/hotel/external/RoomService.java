package hotel.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "room", url = "${api.url.room}")
public interface RoomService {
    @GetMapping(path = "/rooms")
    public List<Room> getRoom();

    @GetMapping(path = "/rooms/{id}")
    public Room getRoom(@PathVariable("id") Long id);
}
