package fd.se.ooad_project.service;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ExpertTask;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.repository.report.ExpertReportRepository;
import fd.se.ooad_project.repository.report.MarketReportRepository;
import fd.se.ooad_project.repository.report.ProductInspectEntryRepository;
import fd.se.ooad_project.service.date.IDateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static fd.se.ooad_project.entity.consts.AuditTaskType.MARKET;

@Service
@Slf4j
@AllArgsConstructor
public class ReportService {

    private final MarketReportRepository marketReportRepository;
    private final ExpertReportRepository expertReportRepository;

    private final ProductInspectEntryRepository entryRepository;

    private final TaskService taskService;
    private IDateService dateService;

    public List<MarketReport> getMarketReports(User market) {
        assert market.getRole() == Role.MARKET;
        return marketReportRepository.findByMarketAndTaskType(market, MARKET);
    }

    public MarketReport getMarketReportById(int id) {
        return marketReportRepository.findById(id);
    }


    public boolean submitMarketReportFromRequest(MarketReportRequest request) {
        MarketReport report = getMarketReportById(request.getId());
        if (report.isSubmitted()) {
            return false;
        }
        request.update(report.getEntries(), dateService.currDate());
        report = marketReportRepository.save(report);
        if (0 != entryRepository.countByReportIdAndArchivedFalse(report.getId())) {
            return false;
        }

        report.setSubmitted(true);
        report.setDateSubmit(dateService.currDate());
        report = marketReportRepository.save(report);

        final AuditTask task = report.getTask();
        final AuditTaskType taskType = task.getType();
        if (taskType == MARKET && taskService.tryCompleteAuditTask(task)) {
            log.info("Market Task {} complete. ", task.getId());
        }
        return true;
    }


    public List<ExpertReport> getExpertReports(User expert) {
        assert expert.getRole() == Role.EXPERT;
        return expertReportRepository.findByTaskExpert(expert);
    }


    public ExpertReport getExpertReportById(int id) {
        return expertReportRepository.findById(id);
    }

    public boolean submitExpertReportOfId(int id) {
        ExpertReport report = getExpertReportById(id);
        if (marketReportRepository.countUnsubmittedMarketReportsOfExpertReport(id) != 0) {
            return false;
        }
        report.setSubmitted(true);
        report.setDateSubmit(dateService.currDate());
        report = expertReportRepository.save(report);

        final ExpertTask task = report.getTask();
        if (taskService.tryCompleteAuditTask(task)) {
            log.info("Expert Task {} complete. ", task.getId());
        }

        return true;
    }


    public List<MarketReport> getExpertReportSubMarketReports(int id) {
        return marketReportRepository.findMarketReportsOfExpertReport(id);
    }

    public void setDateService(IDateService dateService) {
        this.dateService = dateService;
    }
}
