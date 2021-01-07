package fd.se.ooad_project.entity.consts;

import java.time.LocalDate;

import static fd.se.ooad_project.entity.consts.Constants.*;

public enum Performance {

    PUNCTUAL(PUNCTUAL_GRADE),
    TIMEOUT(TIMEOUT_GRADE),
    OVERLATE(OVERLATE_GRADE);

    public final int grading;

    Performance(int grading) {
        this.grading = grading;
    }

    public static Performance evaluate(LocalDate deadline, LocalDate dateSubmit) {
        if (dateSubmit.compareTo(deadline) <= 0) {
            return PUNCTUAL;
        } else if (dateSubmit.minusDays(OVERLATE_DELTA).compareTo(deadline) <= 0) {
            return TIMEOUT;
        } else {
            return OVERLATE;
        }
    }
}
