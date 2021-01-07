package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.ExpertTask;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ExpertReport extends ReportBase {

    @OneToMany
    private List<MarketReport> marketReports;

    @NonNull
    @OneToOne
    private ExpertTask task;

}
