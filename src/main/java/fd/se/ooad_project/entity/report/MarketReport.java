package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.usr.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class MarketReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate dateSubmit;
    private boolean submitted;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductInspectEntry> entries;

    @ManyToOne
    private User market;

    @NonNull
    @ManyToOne
    private AuditTask task;

}
