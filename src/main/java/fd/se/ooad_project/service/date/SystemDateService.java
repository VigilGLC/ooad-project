package fd.se.ooad_project.service.date;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Component
@AllArgsConstructor
@Slf4j
public class LocalDateService implements IDateService {

    @Override
    public LocalDate currDate() {
        return LocalDate.now();
    }

    @Override
    public void trigger() {

    }

}
