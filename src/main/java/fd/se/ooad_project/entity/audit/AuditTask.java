package fd.se.ooad_project.entity.audit;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.usr.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public class AuditTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    @NonNull
    private AuditTaskType auditTaskType;
    @NonNull
    private LocalDate deadline;

    private boolean completed;

    @OneToMany
    private List<ProductType> productTypes = new ArrayList<>();

    @OneToMany
    private List<User> markets = new ArrayList<>();

}
