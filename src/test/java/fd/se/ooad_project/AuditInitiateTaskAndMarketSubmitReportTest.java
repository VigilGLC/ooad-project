package fd.se.ooad_project;

import fd.se.ooad_project.entity.consts.AuditTaskType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.service.ProductService;
import fd.se.ooad_project.service.TaskService;
import fd.se.ooad_project.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AuditInitiateTaskAndMarketSubmitReportTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    static String auditName;
    static List<String> marketNames;
    static List<String> typeNames;
    static AuditTaskInitiateRequest request;


    @BeforeAll
    static void setUp() {
        typeNames = Arrays.asList("A", "B", "C", "D");
        auditName = "audit";
        marketNames = Arrays.asList("marketA", "marketB", "marketC");

        request = new AuditTaskInitiateRequest();
        request.setAuditTaskType(AuditTaskType.MARKET);
        request.setMarkets(marketNames);
        request.setProductTypes(typeNames);
        request.setDeadline(LocalDate.now());
    }

    @Test
    void setUpDatabase() {
        for (String typeName : typeNames) {
            productService.createProductType(typeName);
        }
        userService.createUser(auditName, Role.AUDIT);
        for (String marketName : marketNames) {
            userService.createUser(marketName, Role.MARKET);
        }
    }


    @Test
    void testCreateMarketTask() {
        taskService.createAuditTask(request);
    }

}
