package fd.se.ooad_project.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static Date from(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
