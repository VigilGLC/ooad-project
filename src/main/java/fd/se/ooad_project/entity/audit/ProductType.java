package fd.se.ooad_project.entity.audit;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ProductType {
    @Id
    private String name;
}
