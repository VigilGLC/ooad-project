package fd.se.ooad_project.repository.report;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.entity.report.ProductInspectEntry;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProductInspectEntryRepository extends CrudRepository<ProductInspectEntry, Integer> {

    int countByReportIdAndArchivedFalse(int reportId);


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
            "select sum(entry.unqualified) " +
                    "from ProductInspectEntry entry " +
                    "where " +
                    "   entry.report.task=:task " +
                    "   and " +
                    "   entry.type=:type "
    )
    int sumUnqualifiedInTask(ProductType type, AuditTask task);

}
