package fd.se.ooad_project.service;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ExpertTask;
import fd.se.ooad_project.entity.audit.MarketTask;
import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.report.ProductInspectEntry;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.repository.ProductTypeRepository;
import fd.se.ooad_project.repository.UserRepository;
import fd.se.ooad_project.repository.audittask.ExpertTaskRepository;
import fd.se.ooad_project.repository.audittask.MarketTaskRepository;
import fd.se.ooad_project.repository.report.ExpertReportRepository;
import fd.se.ooad_project.repository.report.MarketReportRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static fd.se.ooad_project.entity.consts.AuditTaskType.MARKET;

@Service
@Slf4j
@AllArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final ProductTypeRepository productTypeRepository;

    private final MarketTaskRepository marketTaskRepository;
    private final ExpertTaskRepository expertTaskRepository;

    private final MarketReportRepository marketReportRepository;
    private final ExpertReportRepository expertReportRepository;

    @Transactional
    public AuditTask createAuditTask(AuditTaskInitiateRequest request) {
        final AuditTaskType auditTaskType = request.getAuditTaskType();
        final List<User> markets = userRepository.findByRoleAndNameIn(Role.MARKET, request.getMarkets());
        final List<ProductType> productTypes = productTypeRepository.findByNameIn(request.getProductTypes());

        AuditTask auditTask;
        if (auditTaskType == MARKET) {
            auditTask = new MarketTask();
        } else {
            final ExpertTask expertTask = new ExpertTask();
            final User expert = userRepository.findByName(request.getExpert());
            expertTask.setExpert(expert);
            auditTask = expertTask;
        }
        auditTask.setType(auditTaskType);
        auditTask.setDeadline(request.getDeadline());
        auditTask.setMarkets(markets);
        auditTask.setProductTypes(productTypes);


        if (auditTaskType == MARKET) {
            final MarketTask marketTask = (MarketTask) auditTask;
            auditTask = marketTaskRepository.save(marketTask);
            createMarketReports(marketTask, markets, productTypes);
        } else {
            final ExpertTask expertTask = (ExpertTask) auditTask;
            auditTask = expertTaskRepository.save(expertTask);
            createExpertReports(expertTask, markets, productTypes);
        }
        return auditTask;
    }


    private List<MarketReport> createMarketReports(AuditTask task,
                                                   List<User> markets, List<ProductType> productTypes) {
        final List<ProductInspectEntry> entries =
                productTypes.stream().map(ProductInspectEntry::of).
                        collect(Collectors.toList());
        final ArrayList<MarketReport> retList = new ArrayList<>(markets.size());
        for (User market : markets) {
            final MarketReport report = MarketReport.of(task);
            report.setMarket(market);
            report.setEntries(entries);
            retList.add(marketReportRepository.save(report));
        }
        return retList;
    }

    @SuppressWarnings("UnusedReturnValue")
    private ExpertReport createExpertReports(ExpertTask task,
                                             List<User> markets, List<ProductType> productTypes) {
        ExpertReport report = ExpertReport.of(task);
        report = expertReportRepository.save(report);
        task = report.getTask();
        final List<MarketReport> marketReports =
                createMarketReports(task, markets, productTypes);
        report.setMarketReports(marketReports);
        return expertReportRepository.save(report);
    }

    @Transactional
    public boolean tryCompleteAuditTask(AuditTask task) {
        if (task.getType() == AuditTaskType.EXPERT) {
            ExpertTask expertTask = expertTaskRepository.findById(task.getId());
            expertTask.setCompleted(true);
            expertTaskRepository.save(expertTask);
            return true;
        } else {
            if (0 != marketReportRepository.countUnsubmittedMarketReport(task.getId())) {
                return false;
            } else {
                MarketTask marketTask = marketTaskRepository.findById(task.getId());
                marketTask.setCompleted(true);
                marketTaskRepository.save(marketTask);
                return true;
            }
        }
    }


    public List<MarketTask> getMarketTasks(boolean completed) {
        return marketTaskRepository.findByCompleted(completed);
    }

    public List<ExpertTask> getExpertTasks(boolean completed) {
        return expertTaskRepository.findByCompleted(completed);
    }

}
