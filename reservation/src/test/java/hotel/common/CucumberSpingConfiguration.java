package hotel.common;

import hotel.ReservationApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { ReservationApplication.class })
public class CucumberSpingConfiguration {}
