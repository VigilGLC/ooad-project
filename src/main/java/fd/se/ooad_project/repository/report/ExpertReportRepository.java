package fd.se.ooad_project.repository.report;

import fd.se.ooad_project.entity.report.ExpertInspectReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertReportRepository extends CrudRepository<ExpertInspectReport, Integer> {
}
