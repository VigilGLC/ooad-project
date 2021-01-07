package fd.se.ooad_project.repository.report;

import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.usr.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertReportRepository extends CrudRepository<ExpertReport, Integer> {

    List<ExpertReport> findByTaskExpert(User expert);

    ExpertReport findById(int id);
}
