package fd.se.ooad_project.controller.usr;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.interceptor.Subject;
import fd.se.ooad_project.interceptor.authorize.Authorized;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.pojo.request.MereNameRequest;
import fd.se.ooad_project.pojo.response.AuditTasksResponse;
import fd.se.ooad_project.service.ProductService;
import fd.se.ooad_project.service.TaskService;
import fd.se.ooad_project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user/audit")
@AllArgsConstructor
@Slf4j
@Authorized(role = Role.ANY)
public class AuditController {

    private final Subject subject;
    private final ProductService productService;
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/productType/add")
    public ResponseEntity<?> addProductType(@RequestBody MereNameRequest request) {
        final String name = request.getName();
        final boolean created = productService.createProductType(name);
        if (created) {
            log.info("Audit {} add product type {}. Success. ", subject, name);
            return ResponseEntity.ok().build();
        } else {
            log.info("Audit {} add product type {}. Failed. ", subject, name);
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/auditTask/initiate")
    public ResponseEntity<?> initiateAuditTask(@RequestBody AuditTaskInitiateRequest request) {
        final AuditTask auditTask = taskService.createAuditTask(request);
        if (auditTask != null) {
            log.info("Audit {} create audit task {}. Success. ", subject, auditTask.getId());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Audit {} create audit task. Failed. ", subject);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auditTasks")
    public ResponseEntity<?> auditTasks(@RequestParam boolean completed) {
        final AuditTasksResponse response = AuditTasksResponse.newResponse();
        response.add(taskService.getMarketTasks(completed));
        response.add(taskService.getExpertTasks(completed));
        log.info("Audit {} get all {} audit tasks. ", subject,
                completed ? "completed" : "uncompleted");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auditTask")
    public ResponseEntity<?> auditTask(@RequestParam int id) {
        final AuditTask task = taskService.getById(id);
        if (task != null) {
            log.info("Audit {} get task {}. ", subject, id);
            return ResponseEntity.ok(task);
        } else {
            log.warn("Audit {} not found task {}. ", subject, id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productType/unqualified")
    public ResponseEntity<?> unqualifiedNumberOfProductType(
            @RequestParam String typeName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        final ProductType productType = productService.getByName(typeName);
        if (productType != null) {
            log.info("Audit {} get unqualified number of product type {}. ", subject, typeName);
            return ResponseEntity.ok(productService.getNumberOfUnqualifiedFromEntries(
                    productType, from, to));
        } else {
            log.warn("Audit {} get not exist product type {}. ", subject, typeName);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auditTask/productTypes")
    public ResponseEntity<?> productTypesForTask(@RequestParam int id, @RequestParam boolean completed) {
        final AuditTask task = taskService.getById(id);
        if (task != null) {
            log.info("Audit {} get uncompleted product type in task {}. ", subject, id);
            return ResponseEntity.ok(productService.getProductTypesInTask(task, completed));
        } else {
            log.warn("Audit {} get not exist audit task {}. ", subject, id);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auditTask/markets/uncompleted")
    public ResponseEntity<?> uncompletedMarketsForTask(@RequestParam int id) {
        final AuditTask task = taskService.getById(id);
        if (task != null) {
            log.info("Audit {} get uncompleted product type in task {}. ", subject, id);
            return ResponseEntity.ok(userService.getUncompletedMarketsInTask(task));
        } else {
            log.warn("Audit {} get not exist audit task {}. ", subject, id);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/auditTask/productType/unqualified")
    public ResponseEntity<?> unqualifiedNumberOfProductTypeForTask(
            @RequestParam String typeName,
            @RequestParam int id) {
        final ProductType productType = productService.getByName(typeName);
        final AuditTask task = taskService.getById(id);
        if (productType != null && task != null) {
            log.info("Audit {} get unqualified number of product type {} in task {}. ", subject, typeName, id);
            return ResponseEntity.ok(productService.
                    getNumberOfUnqualifiedFromEntriesInTask(productType, task));
        } else {
            log.warn("Audit {} get something not exists of product type {} in task {}. ", subject, typeName, id);
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @GetMapping("/user/gradeRecords")
    public ResponseEntity<?> gradeRecordsForUser(
            @RequestParam String name) {
        final User user = userService.getUser(name);
        if (user != null) {
            log.info("Audit {} get grade records for user {}. ", subject, name);
            return ResponseEntity.ok(user.getGradeRecords());
        } else {
            log.warn("Audit {} get user {} not exists. ", subject, name);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users/expert")
    public ResponseEntity<?> allExperts() {
        final List<User> experts = userService.getUsers(Role.EXPERT);
        log.info("Audit {} get all experts. ", subject);
        return ResponseEntity.ok(experts);
    }

    @GetMapping("/users/market")
    public ResponseEntity<?> allMarkets() {
        final List<User> experts = userService.getUsers(Role.MARKET);
        log.info("Audit {} get all markets. ", subject);
        return ResponseEntity.ok(experts);
    }


}
