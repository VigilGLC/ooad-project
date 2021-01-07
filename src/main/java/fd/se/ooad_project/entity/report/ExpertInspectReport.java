package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.ExpertInspectTask;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class ExpertInspectReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate dateReported;
    private boolean reported;

    @OneToMany
    private List<MarketInspectReport> marketReports;
    @OneToOne
    private ExpertInspectTask task;

}
