package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.usr.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class MarketReport extends ReportBase {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProductInspectEntry> entries = new ArrayList<>();

    @ManyToOne
    private User market;

    @NonNull
    @ManyToOne
    private AuditTask task;

}
