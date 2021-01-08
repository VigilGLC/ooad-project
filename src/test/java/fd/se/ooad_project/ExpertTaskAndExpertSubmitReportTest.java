package fd.se.ooad_project;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ExpertTask;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.service.ProductService;
import fd.se.ooad_project.service.ReportService;
import fd.se.ooad_project.service.TaskService;
import fd.se.ooad_project.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ExpertTaskAndExpertSubmitReportTest {


    @Autowired
    private TaskService taskService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    ReportService reportService;

    String auditName;
    String expertName;
    List<String> marketNames;
    List<String> typeNames;
    AuditTaskInitiateRequest request;

    @BeforeEach
    void setUp() {
        typeNames = Arrays.asList("A", "B", "C", "D");
        auditName = "audit";
        expertName = "expert";
        marketNames = Arrays.asList("marketA", "marketB", "marketC");

        request = new AuditTaskInitiateRequest();
        request.setAuditTaskType(AuditTaskType.EXPERT);
        request.setExpert(expertName);
        request.setMarkets(marketNames);
        request.setProductTypes(typeNames);
        request.setDeadline(LocalDate.now());

        for (String typeName : typeNames) {
            productService.createProductType(typeName);
        }
        userService.createUser(auditName, Role.AUDIT);
        userService.createUser(expertName, Role.EXPERT);
        for (String marketName : marketNames) {
            userService.createUser(marketName, Role.MARKET);
        }
        final AuditTask task = taskService.createAuditTask(request);
        Assertions.assertNotNull(task);
    }

    @Test
    public void testExpertTask() {
        final List<ExpertTask> expertTasks = taskService.getExpertTasks(false);
        Assertions.assertEquals(1, expertTasks.size());
        final User expert = userService.getUser(expertName);

        final List<ExpertReport> expertReports = reportService.getExpertReports(expert);
        Assertions.assertEquals(1, expertReports.size());
        ExpertReport report = expertReports.get(0);

        List<MarketReport> marketReports = reportService.
                getExpertReportSubMarketReports(report.getId());
        Assertions.assertEquals(marketNames.size(), marketReports.size());

        Assertions.assertFalse(report.isSubmitted());
        final MarketReportRequest request = new MarketReportRequest();
        request.setEntrySlims(typeNames.stream().map(name ->
                MarketReportRequest.
                        ProductInspectEntrySlim.of(name, 1)).
                collect(Collectors.toList()));
        for (MarketReport marketReport : marketReports) {
            request.setId(marketReport.getId());
            reportService.submitMarketReportFromRequest(request);
            marketReport = reportService.
                    getMarketReportById(marketReport.getId());
            Assertions.assertTrue(marketReport.isSubmitted());
        }
        report = reportService.getExpertReportById(report.getId());
        Assertions.assertFalse(report.isSubmitted());
        Assertions.assertTrue(reportService.submitExpertReportOfId(report.getId()));
        report = reportService.getExpertReportById(report.getId());
        Assertions.assertTrue(report.isSubmitted());
    }

    @Test
    public void testExpertTaskSubmitWithoutCompletion() {
        final List<ExpertTask> expertTasks = taskService.getExpertTasks(false);
        Assertions.assertEquals(1, expertTasks.size());
        final User expert = userService.getUser(expertName);

        final List<ExpertReport> expertReports = reportService.getExpertReports(expert);
        Assertions.assertEquals(1, expertReports.size());
        ExpertReport report = expertReports.get(0);

        Assertions.assertFalse(reportService.submitExpertReportOfId(report.getId()));
    }

}
