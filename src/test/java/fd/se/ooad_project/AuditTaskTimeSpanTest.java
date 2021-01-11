package fd.se.ooad_project;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ExpertTask;
import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Performance;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.report.ExpertReport;
import fd.se.ooad_project.entity.report.MarketReport;
import fd.se.ooad_project.entity.usr.GradeRecord;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.repository.report.MarketReportRepository;
import fd.se.ooad_project.service.*;
import fd.se.ooad_project.service.date.IDateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
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

    @Transactional
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
        /* day3
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
        dateService.skipDate(1);
        final ExpertReport expertReport =
                reportService.getExpertReports(userService.getUser("expert")).get(0);
        reportService.submitExpertReportOfId(expertReport.getId());

        Assertions.assertEquals(31,
                numberOfUnqualified("1", dayBegin, dayBegin.plusDays(2)));
        Assertions.assertEquals(12,
                numberOfUnqualified("2", dayBegin, dayBegin.plusDays(1)));
        Assertions.assertEquals(5,
                numberOfUnqualified("3", dayBegin, dayBegin.plusDays(3)));
        Assertions.assertEquals(25,
                numberOfUnqualified("3", dayBegin.plusDays(3), dayBegin.plusDays(6)));
    }


    @Transactional
    @Test
    void testMarketPunctual() {
        MockDateService dateService = (MockDateService) this.dateService;
        LocalDate deadline = dateService.currDate().plusDays(1);

        final HashMap<String, MarketReport> mapForMarket =
                mapForMarketReport(Arrays.asList("A", "B"),
                        Collections.singletonList("1"), deadline);


        LocalDate dayBegin = dateService.currDate();
        /* day0
           market A: 1,5
        */
        dateService.skipDate(0);
        submitForReport(mapForMarket.get("A"), "1", 20);
        final AuditTask task = mapForMarket.get("A").getTask();
        final List<ProductType> typeList = productService.getProductTypesInTask(task, false);
        Assertions.assertEquals(1, typeList.size());
        Assertions.assertEquals(0,
                userService.getUser("A").getGradeRecords().size());
        /* day1
           market B: 1,3
        */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("B"), "1", 3);
        Assertions.assertEquals(1,
                userService.getUser("A").getGradeRecords().size());
        /* day2
         */
        dateService.skipDate(1);
        final List<GradeRecord> aRecords = userService.getUser("A").getGradeRecords();
        final List<GradeRecord> bRecords = userService.getUser("B").getGradeRecords();
        Assertions.assertEquals(1, aRecords.size());
        Assertions.assertEquals(1, bRecords.size());
        Assertions.assertEquals(Performance.PUNCTUAL.grading, aRecords.get(0).getGrading());
        Assertions.assertEquals(Performance.PUNCTUAL.grading, bRecords.get(0).getGrading());
    }

    @Transactional
    @Test
    void testMarketTimeout() {
        MockDateService dateService = (MockDateService) this.dateService;
        LocalDate deadline = dateService.currDate().plusDays(1);

        final HashMap<String, MarketReport> mapForMarket =
                mapForMarketReport(Arrays.asList("A", "B", "C"),
                        Collections.singletonList("1"), deadline);


        LocalDate dayBegin = dateService.currDate();
        /* day0
         */
        dateService.skipDate(0);
        /* day1
         */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("A"), "1", 20);
        /* day2
         */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("B"), "1", 21);
        /*day3-19
         */
        for (int day = 3; day <= 19; day++) {
            dateService.skipDate(1);
        }
        /* day20
         */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("C"), "1", 22);
        /* day21
         */
        dateService.skipDate(1);

        final List<GradeRecord> aRecords = userService.getUser("A").getGradeRecords();
        final List<GradeRecord> bRecords = userService.getUser("B").getGradeRecords();
        final List<GradeRecord> cRecords = userService.getUser("C").getGradeRecords();

        Assertions.assertEquals(1, aRecords.size());
        Assertions.assertEquals(1, bRecords.size());
        Assertions.assertEquals(1, cRecords.size());

        Assertions.assertEquals(Performance.PUNCTUAL.grading, aRecords.get(0).getGrading());
        Assertions.assertEquals(Performance.TIMEOUT.grading, bRecords.get(0).getGrading());
        Assertions.assertEquals(Performance.TIMEOUT.grading, bRecords.get(0).getGrading());

    }


    @Transactional
    @Test
    void testMarketOverlate() {
        MockDateService dateService = (MockDateService) this.dateService;
        LocalDate deadline = dateService.currDate().plusDays(1);

        final HashMap<String, MarketReport> mapForMarket =
                mapForMarketReport(Arrays.asList("A", "B", "C"),
                        Collections.singletonList("1"), deadline);


        LocalDate dayBegin = dateService.currDate();
        /* day20
         */
        dateService.skipDate(20);
        /* day21
         */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("A"), "1", 20);
        /* day22
         */
        dateService.skipDate(1);
        submitForReport(mapForMarket.get("B"), "1", 21);
        /* day23
         */
        dateService.skipDate(1);
        /* day40
         */
        dateService.skipDate(17);
        submitForReport(mapForMarket.get("C"), "1", 19);
        /* day41
         */
        dateService.skipDate(1);

        final List<GradeRecord> aRecords = userService.getUser("A").getGradeRecords();
        final List<GradeRecord> bRecords = userService.getUser("B").getGradeRecords();
        final List<GradeRecord> cRecords = userService.getUser("C").getGradeRecords();

        Assertions.assertEquals(1, aRecords.size());
        Assertions.assertEquals(1, bRecords.size());
        Assertions.assertEquals(1, cRecords.size());

        Assertions.assertEquals(Performance.TIMEOUT.grading, aRecords.get(0).getGrading());
        Assertions.assertEquals(Performance.OVERLATE.grading, bRecords.get(0).getGrading());
        Assertions.assertEquals(Performance.OVERLATE.grading, cRecords.get(0).getGrading());

    }

    @Transactional
    @Test
    void testExpert() {
        MockDateService dateService = (MockDateService) this.dateService;
        LocalDate deadline = dateService.currDate().plusDays(2);

        final HashMap<String, MarketReport> mapForExpert =
                mapForExpertSubMarketReport(
                        "EXP", Arrays.asList("A", "B"), Arrays.asList("1", "2"), deadline);
        final User expert = userService.getUser("EXP");
        final List<ExpertTask> expertTasks = taskService.getExpertTasks(false);
        final ExpertTask task = expertTasks.get(0);
        final ProductType type1 = productService.getByName("1");
        final ProductType type2 = productService.getByName("2");

        LocalDate dayBegin = dateService.currDate();
        /* day0
         */
        dateService.skipDate(0);
        submitForReport(mapForExpert.get("A"), "1", 5);
        /* day1
         */
        dateService.skipDate(1);
        submitForReport(mapForExpert.get("B"), "1", 5);
        Assertions.assertEquals(10,
                productService.getNumberOfUnqualifiedFromEntriesInTask(type1, task));
        Assertions.assertEquals(0,
                productService.getNumberOfUnqualifiedFromEntriesInTask(type2, task));
        /* day2
         */
        dateService.skipDate(1);
        submitForReport(mapForExpert.get("A"), "2", 5);
        Assertions.assertEquals(5,
                productService.getNumberOfUnqualifiedFromEntriesInTask(type2, task));
        final List<ProductType> typeList = productService.getProductTypesInTask(task, false);
        Assertions.assertEquals(1, typeList.size());
        Assertions.assertEquals(type2.getName(), typeList.get(0).getName());
        /* day3
         */
        dateService.skipDate(1);
        submitForReport(mapForExpert.get("B"), "2", 5);
        final List<ExpertReport> reports = reportService.getExpertReports(expert);
        Assertions.assertEquals(1, reports.size());
        reportService.submitExpertReportOfId(reports.get(0).getId());
        /* day4
         */
        dateService.skipDate(1);

        final List<GradeRecord> eRecords = expert.getGradeRecords();

        Assertions.assertEquals(1, eRecords.size());

        Assertions.assertEquals(Performance.TIMEOUT.grading, eRecords.get(0).getGrading());

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

    private int numberOfUnqualified(String typeName, LocalDate from, LocalDate to) {
        final ProductType type = productService.getByName(typeName);
        return productService.getNumberOfUnqualifiedFromEntries(type, from, to);
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
