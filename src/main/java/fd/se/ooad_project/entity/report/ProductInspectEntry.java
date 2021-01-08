package fd.se.ooad_project.entity.report;


import fd.se.ooad_project.entity.audit.ProductType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ProductInspectEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductType type;
    private int unqualified;

    private boolean archived;
    private LocalDate dateArchived;

    @ManyToOne
    private MarketReport report;
}
