package fd.se.ooad_project.repository.report;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketReportRepository extends CrudRepository<MarketReport, Integer> {

    List<MarketReport> findByMarketAndTaskAuditTaskType(User market, AuditTaskType type);

    MarketReport findById(int id);

    @Query(value =
            "select markertReport " +
                    "from ExpertReport expertReport, MarketReport markertReport " +
                    "where " +
                    "   expertReport.id=:expertReportId" +
                    "   and " +
                    "   expertReport.task=markertReport.task "
    )
    List<MarketReport> findMarketReportsOfExpertReport(int expertReportId);

    @Query(value =
            "select count(markertReport) " +
                    "from ExpertReport expertReport, MarketReport markertReport " +
                    "where " +
                    "   expertReport.id=:expertReportId" +
                    "   and " +
                    "   expertReport.task=markertReport.task " +
                    "   and " +
                    "   expertReport.submitted=false "
    )
    int countUnsubmittedMarketReportsOfExpertReport(int expertReportId);

}
