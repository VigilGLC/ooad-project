package fd.se.ooad_project.pojo.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor(staticName = "of")
public class DateResponse {
    private LocalDate date;
}
