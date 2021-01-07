package fd.se.ooad_project.entity.audit;


import fd.se.ooad_project.entity.usr.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class ExpertTask extends AuditTask {

    @ManyToOne
    private User expert;

}
