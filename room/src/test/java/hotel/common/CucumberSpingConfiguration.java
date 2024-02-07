package hotel.common;

import hotel.RoomApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { RoomApplication.class })
public class CucumberSpingConfiguration {}
