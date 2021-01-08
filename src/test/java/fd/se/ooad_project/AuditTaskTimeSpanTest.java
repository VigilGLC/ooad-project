package fd.se.ooad_project;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.repository.report.MarketReportRepository;
import fd.se.ooad_project.service.*;
import fd.se.ooad_project.service.date.IDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuditTaskTimeSpanTest {

    @Autowired
    private MarketReportRepository marketReportRepository;


    @Autowired
    private TaskService taskService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Autowired
    private IDateService dateService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private IndicatorService indicatorService;

    String auditName = "audit";


    @BeforeEach
    void setUp() {
        final MockDateService mockDateService = new MockDateService();
        mockDateService.setPublisher(dateService.getPublisher());
        mockDateService.setCurrDate(LocalDate.now());
        reportService.setDateService(mockDateService);
        indicatorService.setDateService(mockDateService);
        dateService = mockDateService;

        userService.createUser(auditName, Role.AUDIT);
    }

    @Test
    void testUnqualified() {
        MockDateService dateService = (MockDateService) this.dateService;

        LocalDate deadlineForMarketTask = dateService.currDate().plusDays(2);
        LocalDate deadlineForExpertTask = dateService.currDate().plusDays(3);

        final HashMap<String, MarketReport> mapForMarket =
                mapForMarketReport(Arrays.asList("A", "B"),
                        Arrays.asList("1", "2"), deadlineForMarketTask);
        final HashMap<String, MarketReport> mapForExpert =
                mapForExpertSubMarketReport("expert",
                        Arrays.asList("A", "C"), Arrays.asList("1", "3"),
                        deadlineForExpertTask);

        LocalDate dayBegin = dateService.currDate();
        /* day0
           market A: 1,20   2,5
           market B: 2,7
           expert:
             market A: 1,5
             market C: 1,6
        */
        dateService.skipDate(0);
        submitForReport(mapForMarket.get("A"), "1", 20);
        submitForReport(mapForMarket.get("A"), "2", 5);
        submitForReport(mapForMarket.get("B"), "2", 7);
        submitForReport(mapForExpert.get("A"), "1", 5);
        submitForReport(mapForExpert.get("C"), "1", 6);
        /* day1
           expert:
             market C: 3,5
        */
        dateService.skipDate(1);
        submitForReport(mapForExpert.get("C"), "3", 5);
        /* day2
         */
        dateService.skipDate(1);
        /* day0
           market B: 1,9
        */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("B"), "1", 9);
        /* day4
           expert:
             market A: 3,25
        */
        dateService.skipDate(1);
        submitForReport(mapForExpert.get("A"), "3", 25);
        /* day5
         */
        reportService.submitExpertReportOfId(
                reportService.getExpertReports(userService.getUser("expert")).get(0).getId());
    }


    private HashMap<String, MarketReport> mapForMarketReport(List<String> marketNames,
                                                             List<String> typeNames,
                                                             LocalDate deadline) {
        for (String marketName : marketNames) {
            userService.createUser(marketName, Role.MARKET);
        }
        for (String typeName : typeNames) {
            productService.createProductType(typeName);
        }

        AuditTaskInitiateRequest marketTaskRequest = new AuditTaskInitiateRequest();
        marketTaskRequest.setAuditTaskType(AuditTaskType.MARKET);
        marketTaskRequest.setDeadline(deadline);
        marketTaskRequest.setProductTypes(typeNames);
        marketTaskRequest.setMarkets(marketNames);
        final AuditTask marketTask = taskService.createAuditTask(marketTaskRequest);

        final HashMap<String, MarketReport> map = new HashMap<>(marketNames.size());
        for (String marketName : marketNames) {
            map.put(marketName, marketReportRepository.findByTaskAndMarketName(marketTask, marketName));
        }
        return map;
    }

    private HashMap<String, MarketReport> mapForExpertSubMarketReport(String expertName,
                                                                      List<String> marketNames,
                                                                      List<String> typeNames,
                                                                      LocalDate deadline) {
        userService.createUser(expertName, Role.EXPERT);
        for (String marketName : marketNames) {
            userService.createUser(marketName, Role.MARKET);
        }
        for (String typeName : typeNames) {
            productService.createProductType(typeName);
        }

        AuditTaskInitiateRequest marketTaskRequest = new AuditTaskInitiateRequest();
        marketTaskRequest.setExpert(expertName);
        marketTaskRequest.setAuditTaskType(AuditTaskType.EXPERT);
        marketTaskRequest.setDeadline(deadline);
        marketTaskRequest.setProductTypes(typeNames);
        marketTaskRequest.setMarkets(marketNames);
        final AuditTask marketTask = taskService.createAuditTask(marketTaskRequest);

        final HashMap<String, MarketReport> map = new HashMap<>(marketNames.size());
        for (String marketName : marketNames) {
            map.put(marketName, marketReportRepository.findByTaskAndMarketName(marketTask, marketName));
        }
        return map;
    }


    private void submitForReport(MarketReport report, String typeName, int unqualified) {
        final MarketReportRequest request = buildRequest(typeName, unqualified);
        request.setId(report.getId());
        reportService.submitMarketReportFromRequest(request);
    }

    private static MarketReportRequest buildRequest(String name, int unqualified) {
        final MarketReportRequest request = new MarketReportRequest();
        final MarketReportRequest.ProductInspectEntrySlim entrySlim =
                MarketReportRequest.ProductInspectEntrySlim.of(name, unqualified);
        request.setEntrySlims(Collections.singletonList(entrySlim));
        return request;
    }


}
