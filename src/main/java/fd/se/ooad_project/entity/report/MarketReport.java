package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.usr.User;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class MarketReport extends ReportBase {

    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductInspectEntry> entries;

    @ManyToOne
    private User market;

    @NonNull
    @ManyToOne
    private AuditTask task;

}
