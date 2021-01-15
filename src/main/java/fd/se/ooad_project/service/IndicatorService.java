package fd.se.ooad_project.service;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.repository.report.ExpertReportRepository;
import fd.se.ooad_project.repository.report.MarketReportRepository;
import fd.se.ooad_project.service.date.DateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class IndicatorService implements ApplicationListener<DateEvent> {

    private final MarketReportRepository marketReportRepository;
    private final ExpertReportRepository expertReportRepository;

    private final GradingService gradingService;

    @Transactional
    public void update() {
        final List<MarketReport> marketReports = marketReportRepository.
                findByTaskTypeAndSubmittedTrueAndGradedFalse(AuditTaskType.MARKET);
        final List<ExpertReport> expertReports = expertReportRepository.
                findBySubmittedTrueAndGradedFalse();

        for (MarketReport marketReport : marketReports) {
            gradingService.recordGradingForReport(marketReport);
            marketReport.setGraded(true);
            marketReportRepository.save(marketReport);
        }
        for (ExpertReport expertReport : expertReports) {
            gradingService.recordGradingForReport(expertReport);
            expertReport.setGraded(true);
            expertReportRepository.save(expertReport);
        }
    }

    @Override
    public void onApplicationEvent(DateEvent event) {
        log.info("Date Event Received, start update grades. ");
        update();
    }
}
