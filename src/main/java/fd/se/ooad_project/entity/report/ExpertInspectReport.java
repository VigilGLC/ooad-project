package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.ExpertTask;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ExpertInspectReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate dateReported;
    private boolean reported;

    @OneToMany
    private List<MarketReport> marketReports;

    @NonNull
    @OneToOne
    private ExpertTask task;

}
