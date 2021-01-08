package fd.se.ooad_project.service.date;


import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;

public interface IDateService {

    LocalDate currDate();

    void trigger();

    ApplicationEventPublisher getPublisher();
}
