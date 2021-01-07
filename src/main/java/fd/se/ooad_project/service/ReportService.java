package fd.se.ooad_project.service;

import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.repository.report.ExpertReportRepository;
import fd.se.ooad_project.repository.report.MarketReportRepository;
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

    public List<MarketReport> getMarketReports(User market) {
        assert market.getRole() == Role.MARKET;
        return marketReportRepository.findByMarketAndTaskAuditTaskType(market, MARKET);
    }

    public MarketReport getMarketReportById(int id) {
        return marketReportRepository.findById(id);
    }


    public boolean submitMarketReportFromRequest(MarketReportRequest request) {
        final MarketReport report = getMarketReportById(request.getId());
        if (report.isSubmitted()) {
            return false;
        }
        request.update(report.getEntries());
        report.setSubmitted(true);
        marketReportRepository.save(report);
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
        final ExpertReport report = getExpertReportById(id);
        if (marketReportRepository.countUnsubmittedMarketReportsOfExpertReport(id) != 0) {
            return false;
        }
        report.setSubmitted(true);
        expertReportRepository.save(report);
        return true;
    }


    public List<MarketReport> getExpertReportSubMarketReports(int id) {
        return marketReportRepository.findMarketReportsOfExpertReport(id);
    }

}
