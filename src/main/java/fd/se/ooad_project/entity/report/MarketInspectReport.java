package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.MarketInspectTask;
import fd.se.ooad_project.entity.usr.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class MarketInspectReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate dateReported;
    private boolean reported;

    @OneToMany
    private List<ProductTypeInspectEntry> entries;

    @ManyToOne
    private User market;
    @ManyToOne
    private AuditTask task;

}
