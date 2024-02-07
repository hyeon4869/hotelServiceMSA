package hotel.infra;

import hotel.domain.*;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Transactional
public class RoomController {

    private final RoomRepository roomRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerRoom(@RequestBody RoomCreated roomCreated){
        Room room = new Room();
        room.setRoomType(roomCreated.getRoomType());
        if(!roomCreated.getStatus().equals("예약")){
            room.setStatus("empty");
        }
        room.setFeatures(roomCreated.getFeatures());
        room.setPricePerNight(roomCreated.getPricePerNight());
        room.setRoomNumber(roomCreated.getRoomNumber());
        roomRepository.save(room);
        return ResponseEntity.ok("룸 등록 완료");
    }

    @GetMapping
    public ResponseEntity<List<Room>> findAll(){
        List<Room> rooms = (List<Room>) roomRepository.findAll();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable("id") Long id){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if(optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            return ResponseEntity.ok(room);
        } else {
            return null;
        }
    }

}
//>>> Clean Arch / Inbound Adaptor
