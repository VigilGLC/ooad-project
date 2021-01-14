package fd.se.ooad_project;


import fd.se.ooad_project.service.date.DateEvent;
import fd.se.ooad_project.service.date.IDateService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;


@Slf4j
@Setter
@Getter
public class MockDateService implements IDateService {

    private ApplicationEventPublisher publisher;

    private LocalDate currDate;

    public void setCurrDate(LocalDate date) {
        if (currDate == null) {
            this.currDate = date;
        }
    }

    public void skipDate(long days) {
        if (days <= 0) return;
        for (int i = 0; i < days; i++) {
            this.currDate = this.currDate.plusDays(1);
            trigger();
        }
    }


    @Override
    public LocalDate currDate() {
        return currDate;
    }

    @Override
    public void trigger() {
        log.info("Date Event Triggered on {}", currDate());
        final DateEvent dateEvent = DateEvent.of(this);
        publisher.publishEvent(dateEvent);
    }
}
