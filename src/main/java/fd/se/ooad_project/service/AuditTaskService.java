package fd.se.ooad_project.service;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ExpertInspectTask;
import fd.se.ooad_project.entity.audit.MarketInspectTask;
import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.MarketInspectReport;
import fd.se.ooad_project.entity.report.ProductTypeInspectEntry;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.repository.MarketInspectReportRepository;
import fd.se.ooad_project.repository.MarketInspectTaskRepository;
import fd.se.ooad_project.repository.ProductTypeRepository;
import fd.se.ooad_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static fd.se.ooad_project.entity.consts.AuditTaskType.MARKET;

@Service
@Slf4j
@AllArgsConstructor
public class AuditTaskService {

    private final UserRepository userRepository;
    private final ProductTypeRepository productTypeRepository;

    private final MarketInspectTaskRepository marketInspectTaskRepository;
    private final MarketInspectReportRepository marketInspectReportRepository;

    public AuditTask createAuditTask(AuditTaskInitiateRequest request) {
        final AuditTaskType auditTaskType = request.getAuditTaskType();
        final List<User> markets = userRepository.findByRoleAndNameIn(Role.MARKET, request.getMarkets());
        final List<ProductType> productTypes = productTypeRepository.findByNameIn(request.getProductTypes());

        AuditTask auditTask;
        if (auditTaskType == MARKET) {
            auditTask = new MarketInspectTask();
        } else {
            final ExpertInspectTask expertInspectTask = new ExpertInspectTask();
            final User expert = userRepository.findByName(request.getExpert());
            expertInspectTask.setExpert(expert);
            auditTask = expertInspectTask;
        }
        auditTask.setAuditTaskType(auditTaskType);
        auditTask.setDeadline(request.getDeadline());


        auditTask.setMarkets(markets);
        auditTask.setProductTypes(productTypes);

        auditTask = auditTaskRepository.save(auditTask);


        if (auditTaskType == MARKET) {

        }


        return auditTask;
    }

    private MarketInspectTask createMarketInspectTask(LocalDate deadline) {
        final MarketInspectTask task = new MarketInspectTask();
        task.setAuditTaskType(MARKET);
        task.setDeadline(deadline);
        return task;
    }

    private void createMarketInspectReports(MarketInspectTask task,
                                            List<User> markets, List<ProductType> productTypes) {
        final List<ProductTypeInspectEntry> entries =
                productTypes.stream().map(ProductTypeInspectEntry::of).
                        collect(Collectors.toList());
        for (User market : markets) {
            final MarketInspectReport report = new MarketInspectReport();
            report.setMarket(market);
            report.setEntries(entries);
            report.setTask(task);
            marketInspectReportRepository.save(report);
        }
    }


}
