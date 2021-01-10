package fd.se.ooad_project.entity.audit;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.usr.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

    @Version
    private int version;

    private String description;

    @Enumerated(EnumType.STRING)
    @NonNull
    private AuditTaskType type;
    @NonNull
    private LocalDate deadline;

    private boolean completed;

    @ManyToMany
    private List<ProductType> productTypes = new ArrayList<>();

    @ManyToMany
    private List<User> markets = new ArrayList<>();

}
