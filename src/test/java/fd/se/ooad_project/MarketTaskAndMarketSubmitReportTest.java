package fd.se.ooad_project;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.service.ProductService;
import fd.se.ooad_project.service.ReportService;
import fd.se.ooad_project.service.TaskService;
import fd.se.ooad_project.service.UserService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.runners.MethodSorters.NAME_ASCENDING;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@FixMethodOrder(NAME_ASCENDING)
class MarketTaskAndMarketSubmitReportTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    String auditName;
    List<String> marketNames;
    List<String> typeNames;
    AuditTaskInitiateRequest request;


    @BeforeEach
    void setUp() {
        typeNames = Arrays.asList("A", "B", "C", "D");
        auditName = "audit";
        marketNames = Arrays.asList("marketA", "marketB", "marketC");

        request = new AuditTaskInitiateRequest();
        request.setAuditTaskType(AuditTaskType.MARKET);
        request.setMarkets(marketNames);
        request.setProductTypes(typeNames);
        request.setDeadline(LocalDate.of(2000,1,1));
    }

    @Test
    void testMarketTaskAllMarketsComplete() {
        for (String typeName : typeNames) {
            productService.createProductType(typeName);
        }
        userService.createUser(auditName, Role.AUDIT);
        for (String marketName : marketNames) {
            userService.createUser(marketName, Role.MARKET);
        }
        final AuditTask task = taskService.createAuditTask(request);
        Assertions.assertNotNull(task);

        HashMap<String, MarketReport> map = new HashMap<>(marketNames.size());
        for (String marketName : marketNames) {
            User market = userService.getUser(marketName);
            final List<MarketReport> reports = reportService.getMarketReports(market);
            Assertions.assertEquals(1, reports.size());
            final MarketReport report = reports.get(0);
            Assertions.assertEquals(task.getId(), report.getTask().getId());
            map.put(marketName, report);
        }

        final MarketReportRequest request = new MarketReportRequest();
        request.setEntrySlims(typeNames.stream().map(name ->
                MarketReportRequest.
                        ProductInspectEntrySlim.of(name, 1)).
                collect(Collectors.toList()));
        int remains = marketNames.size();
        for (String marketName : marketNames) {
            final MarketReport report = map.get(marketName);
            request.setId(report.getId());
            Assertions.assertEquals(remains, userService.getUncompletedMarketsInTask(task).size());
            remains--;
            reportService.submitMarketReportFromRequest(request);
        }
        Assertions.assertEquals(remains, userService.getUncompletedMarketsInTask(task).size());
        Assertions.assertTrue(taskService.getById(task.getId()).isCompleted());
    }

    @Test
    void testMarketTaskOneMarketsUncompleted() {
        typeNames = typeNames.subList(0, 2);
        marketNames = marketNames.subList(0, 1);
        for (String typeName : typeNames) {
            productService.createProductType(typeName);
        }
        userService.createUser(auditName, Role.AUDIT);
        for (String marketName : marketNames) {
            userService.createUser(marketName, Role.MARKET);
        }
        final User market = userService.getUser(marketNames.get(0));
        request.setMarkets(marketNames);
        request.setProductTypes(typeNames);
        final AuditTask task = taskService.createAuditTask(request);
        Assertions.assertNotNull(task);

        Assertions.assertEquals(1, taskService.getMarketTasks(false).size());
        final MarketReportRequest request = new MarketReportRequest();
        final List<MarketReportRequest.ProductInspectEntrySlim> entrySlims =
                typeNames.stream().map(name ->
                        MarketReportRequest.
                                ProductInspectEntrySlim.of(name, 1)).
                        collect(Collectors.toList());
        request.setEntrySlims(entrySlims.subList(0, 1));
        final int reportId = reportService.getMarketReports(market).get(0).getId();
        request.setId(reportId);
        reportService.submitMarketReportFromRequest(request);
        Assertions.assertEquals(1, taskService.getMarketTasks(false).size());
        request.setEntrySlims(entrySlims);
        reportService.submitMarketReportFromRequest(request);
        Assertions.assertEquals(0, taskService.getMarketTasks(false).size());
    }


}
