package fd.se.ooad_project.repository;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ProductType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductTypeRepository extends CrudRepository<ProductType, String> {

    ProductType findByName(String name);

    List<ProductType> findByNameIn(List<String> names);


    @Query(value =
            "select sum(entry.unqualified) " +
                    "from ProductInspectEntry entry " +
                    "where " +
                    "   entry.type=:type " +
                    "   and" +
                    "   entry.dateArchived>=:from " +
                    "   and " +
                    "   entry.dateArchived<=:to "
    )
    int sumUnqualifiedBetween(ProductType type, LocalDate from, LocalDate to);


    @Query(value =
            "select distinct entry.type " +
                    "from MarketReport report, ProductInspectEntry entry " +
                    "where " +
                    "   report.task=:task " +
                    "   and " +
                    "   entry member of report.entries " +
                    "   and " +
                    "   entry.archived=false "
    )
    public List<ProductType> findUncompletedProductTypesInTask(AuditTask task);

}
