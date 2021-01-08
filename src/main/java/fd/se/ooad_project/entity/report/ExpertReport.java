package fd.se.ooad_project.entity.report;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fd.se.ooad_project.entity.audit.ExpertTask;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ExpertReport extends ReportBase {

    @JsonIgnore
    @OneToMany
    private List<MarketReport> marketReports = new ArrayList<>();

    @NonNull
    @OneToOne
    private ExpertTask task;

}
