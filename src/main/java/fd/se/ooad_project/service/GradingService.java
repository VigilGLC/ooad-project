package fd.se.ooad_project.service;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.consts.Performance;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.report.ReportBase;
import fd.se.ooad_project.entity.usr.GradeRecord;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDate;

@Service
@Slf4j
@AllArgsConstructor
public class GradingService {

    private final UserRepository userRepository;

    @Transactional
    public void recordGradingForReport(ReportBase report) {
        final AuditTask task = report.getTask();
        final LocalDate deadline = task.getDeadline();
        final LocalDate dateSubmit = report.getDateSubmit();
        final Performance performance = Performance.evaluate(deadline, dateSubmit);
        User user = getReportSubmitter(report);
        final GradeRecord record = GradeRecord.
                of(user, evaluateDetails(task, performance), performance.grading);
        record.setTask(task);
        user.getGradeRecords().add(record);
        userRepository.save(user);
    }

    private User getReportSubmitter(ReportBase report) {
        if (report instanceof MarketReport) {
            return ((MarketReport) report).getMarket();
        } else {
            return ((ExpertReport) report).getTask().getExpert();
        }
    }


    private String evaluateDetails(AuditTask task, Performance performance) {
        return MessageFormat.format("Task: {0}, Cause: {1}, Grading: {2}",
                task.getId(), performance.toString().toLowerCase(),
                performance.grading);
    }

}
