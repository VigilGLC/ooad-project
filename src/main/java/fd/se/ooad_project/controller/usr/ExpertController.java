package fd.se.ooad_project.controller.usr;


import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.interceptor.Subject;
import fd.se.ooad_project.interceptor.authorize.Authorized;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.pojo.request.MereIdRequest;
import fd.se.ooad_project.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/expert")
@AllArgsConstructor
@Slf4j
@Authorized(role = Role.ANY)
public class ExpertController {

    private final Subject subject;
    private final ReportService reportService;

    @GetMapping("/expertReports")
    public ResponseEntity<?> allExpertReports() {
        final User currUser = subject.getUser();
        log.info("Expert {} get all expert reports. ", currUser.getName());
        return ResponseEntity.ok(reportService.getExpertReports(currUser));
    }

    @GetMapping("/expertReport")
    public ResponseEntity<?> expertReport(@RequestParam int id) {
        final User currUser = subject.getUser();
        log.info("Expert {} get expert report {}. ", currUser.getName(), id);
        return ResponseEntity.ok(reportService.getExpertReportById(id));
    }

    @PostMapping("/expertReport/submit")
    public ResponseEntity<?> submitExpertReport(@RequestBody MereIdRequest request) {
        final User currUser = subject.getUser();
        final int id = request.getId();
        final boolean result = reportService.submitExpertReportOfId(id);
        if (result) {
            log.info("Expert {} submit expert report {}. Success. ", currUser.getName(), id);
            return ResponseEntity.ok().build();
        } else {
            log.info("Expert {} submit expert report {}. Failed. ", currUser.getName(), id);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/expertReport/marketReports")
    public ResponseEntity<?> subMarketReports(@RequestParam int id) {
        final User currUser = subject.getUser();
        log.info("Expert {} get all sub market reports of expert report {}. ", currUser.getName(), id);
        return ResponseEntity.ok(reportService.getExpertReportSubMarketReports(id));
    }

    @GetMapping("/expertReport/marketReport")
    public ResponseEntity<?> subMarketReport(@RequestParam int id) {
        final User currUser = subject.getUser();
        log.info("Expert {} get sub market report {}. ", currUser.getName(), id);
        return ResponseEntity.ok(reportService.getMarketReportById(id));
    }

    @PostMapping("/expertReport/marketReport/submit")
    public ResponseEntity<?> submitSubMarketReport(@RequestBody MarketReportRequest request) {
        final User currUser = subject.getUser();
        final boolean result = reportService.submitMarketReportFromRequest(request);
        if (result) {
            log.info("Expert {} submit sub market report {}. Success. ", currUser.getName(), request.getId());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Expert {} submit sub market report {}. Failed. ", currUser.getName(), request.getId());
            return ResponseEntity.badRequest().build();
        }
    }


}
