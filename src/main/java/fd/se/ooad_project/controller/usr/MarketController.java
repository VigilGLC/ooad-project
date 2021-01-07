package fd.se.ooad_project.controller.usr;


import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.interceptor.Subject;
import fd.se.ooad_project.interceptor.authorize.Authorized;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/market")
@AllArgsConstructor
@Slf4j
@Authorized(role = Role.MARKET)
public class MarketController {

    private final Subject subject;
    private final ReportService reportService;

    @GetMapping("/marketReports")
    public ResponseEntity<?> allMarketReports() {
        final User currUser = subject.getUser();
        log.info("Market {} get all market reports. ", currUser.getName());
        return ResponseEntity.ok(reportService.getMarketReports(currUser));
    }

    @GetMapping("/marketReport")
    public ResponseEntity<?> marketReport(@RequestParam int id) {
        final User currUser = subject.getUser();
        log.info("Market {} get market report {}. ", currUser.getName(), id);
        return ResponseEntity.ok(reportService.getMarketReportById(id));
    }

    @PostMapping("/marketReport/submit")
    public ResponseEntity<?> submitMarketReport(@RequestBody MarketReportRequest request) {
        final User currUser = subject.getUser();
        final boolean result = reportService.submitMarketReportFromRequest(request);
        if (result) {
            log.info("Market {} submit market report {}. Success. ", currUser.getName(), request.getId());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Market {} submit market report {}. Failed. ", currUser.getName(), request.getId());
            return ResponseEntity.badRequest().build();
        }
    }


}
