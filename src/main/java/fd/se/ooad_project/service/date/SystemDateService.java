package fd.se.ooad_project.service.date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Component
@AllArgsConstructor
@Slf4j
@Getter
public class SystemDateService implements IDateService {

    private final ApplicationEventPublisher publisher;

    @Override
    public LocalDate currDate() {
        return LocalDate.now();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void trigger() {
        log.info("Date Event Triggered on {}", currDate());
        final DateEvent dateEvent = DateEvent.of(this);
        publisher.publishEvent(dateEvent);
    }

}
