package fd.se.ooad_project.repository.report;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.User;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

@Repository
public interface MarketReportRepository extends CrudRepository<MarketReport, Integer> {

    List<MarketReport> findByMarketAndTaskType(User market, AuditTaskType type);

    List<MarketReport> findByTaskTypeAndDateSubmit(AuditTaskType type, LocalDate date);

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

    @Lock(OPTIMISTIC_FORCE_INCREMENT)
    @Query(value =
            "select count(marketReport) " +
                    "from MarketReport marketReport " +
                    "where " +
                    "   marketReport.task.id=:marketTaskId " +
                    "   and " +
                    "   marketReport.submitted=false "
    )
    int countUnsubmittedMarketReport(int marketTaskId);

    @Query(value =
            "select count(entry) " +
                    "from MarketReport marketReport, ProductInspectEntry entry " +
                    "where " +
                    "   marketReport.id=:reportId " +
                    "   and " +
                    "   entry member of marketReport.entries " +
                    "   and " +
                    "   entry.archived=false "
    )
    int countUnarchivedProductionInspectEntries(int reportId);

}
