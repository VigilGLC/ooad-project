package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.ProductType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ProductInspectEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    @ManyToOne
    private ProductType type;
    private int unqualified;
    private boolean archived;
}
