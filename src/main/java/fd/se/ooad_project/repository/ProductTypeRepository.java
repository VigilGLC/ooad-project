package fd.se.ooad_project.repository;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ProductType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends CrudRepository<ProductType, String> {

    ProductType findByName(String name);

    List<ProductType> findByNameIn(List<String> names);


    @Query(value =
            "select distinct entry.type " +
                    "from MarketReport report, ProductInspectEntry entry " +
                    "where " +
                    "   report.task=:task " +
                    "   and " +
                    "   entry.report=report " +
                    "   and " +
                    "   entry.archived=false "
    )
    List<ProductType> findUncompletedProductTypesInTask(AuditTask task);



}
