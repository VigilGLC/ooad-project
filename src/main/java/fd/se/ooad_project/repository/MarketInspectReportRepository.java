package fd.se.ooad_project.repository;


import fd.se.ooad_project.entity.report.MarketInspectReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketInspectReportRepository extends CrudRepository<MarketInspectReport, Integer> {
}
