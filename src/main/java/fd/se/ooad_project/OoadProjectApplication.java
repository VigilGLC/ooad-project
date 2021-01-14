package fd.se.ooad_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OoadProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(OoadProjectApplication.class, args);
    }

}
