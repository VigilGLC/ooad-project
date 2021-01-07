package fd.se.ooad_project.controller.usr;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.interceptor.Subject;
import fd.se.ooad_project.interceptor.authorize.Authorized;
import fd.se.ooad_project.pojo.request.AuditTaskInitiateRequest;
import fd.se.ooad_project.pojo.request.MereNameRequest;
import fd.se.ooad_project.service.ProductService;
import fd.se.ooad_project.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/audit")
@AllArgsConstructor
@Slf4j
@Authorized(role = Role.ANY)
public class AuditController {

    private final Subject subject;
    private final ProductService productService;
    private final TaskService taskService;

    @PostMapping("/productType/add")
    public ResponseEntity<?> addProductType(@RequestBody MereNameRequest request) {
        final String name = request.getName();
        final boolean created = productService.createProductType(name);
        if (created) {
            log.info("Audit {} add product type {}. Success. ", subject, name);
        } else {
            log.info("Audit {} add product type {}. Failed. ", subject, name);
        }
        return ResponseEntity.ok().build();
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


}
