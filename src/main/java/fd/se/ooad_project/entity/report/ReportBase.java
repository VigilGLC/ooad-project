package fd.se.ooad_project.entity.report;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fd.se.ooad_project.entity.audit.AuditTask;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class ReportBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private LocalDate dateSubmit;
    private boolean submitted;

    public abstract AuditTask getTask();

    @JsonIgnore
    private boolean graded;
}
