package fd.se.ooad_project.entity.usr;


import fd.se.ooad_project.entity.consts.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class User {
    @NonNull
    @Id
    private String name;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GradeRecord> gradeRecords;
}
