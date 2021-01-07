package fd.se.ooad_project.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class LocalDateService implements IDateService {

    @Override
    public LocalDate currDate() {
        return LocalDate.now();
    }

}
