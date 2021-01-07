package fd.se.ooad_project.repository.report;


import fd.se.ooad_project.entity.report.MarketReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketReportRepository extends CrudRepository<MarketReport, Integer> {
}
