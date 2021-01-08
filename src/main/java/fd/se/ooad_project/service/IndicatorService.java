package fd.se.ooad_project.service;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.repository.report.ExpertReportRepository;
import fd.se.ooad_project.repository.report.MarketReportRepository;
import fd.se.ooad_project.service.date.DateEvent;
import fd.se.ooad_project.service.date.IDateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class IndicatorService implements ApplicationListener<DateEvent> {

    private final MarketReportRepository marketReportRepository;
    private final ExpertReportRepository expertReportRepository;

    private IDateService dateService;
    private final GradingService gradingService;

    @Transactional
    public void update() {
        LocalDate yesterday = dateService.currDate().minusDays(1);
        final List<MarketReport> marketReports = marketReportRepository.
                findByTaskTypeAndDateSubmit(AuditTaskType.MARKET, yesterday);
        final List<ExpertReport> expertReports = expertReportRepository.
                findByDateSubmit(yesterday);

        for (MarketReport marketReport : marketReports) {
            gradingService.recordGradingForReport(marketReport);
        }
        for (ExpertReport expertReport : expertReports) {
            gradingService.recordGradingForReport(expertReport);
        }
    }

    @Override
    public void onApplicationEvent(DateEvent event) {
        log.info("Date Event Received, start update grades. ");
        update();
    }

    public void setDateService(IDateService dateService) {
        this.dateService = dateService;
    }
}
