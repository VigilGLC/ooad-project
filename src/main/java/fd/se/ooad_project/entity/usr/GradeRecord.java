package fd.se.ooad_project.entity.usr;


import fd.se.ooad_project.entity.audit.AuditTask;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class GradeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    private String detail;
    @NonNull
    private int grading;

    @ManyToOne
    private AuditTask task;

}
