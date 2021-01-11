package fd.se.ooad_project.controller.usr;


import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.interceptor.Subject;
import fd.se.ooad_project.interceptor.authorize.Authorized;
import fd.se.ooad_project.pojo.request.MarketReportRequest;
import fd.se.ooad_project.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        log.info("Market {} get all market reports. ", subject);
        return ResponseEntity.ok(reportService.getMarketReports(subject.getUser()));
    }

    @GetMapping("/marketReport")
    public ResponseEntity<?> marketReport(@RequestParam int id) {
        log.info("Market {} get market report {}. ", subject, id);
        return ResponseEntity.ok(reportService.getMarketReportById(id));
    }

    @PostMapping("/marketReport/submit")
    public ResponseEntity<?> submitMarketReport(@RequestBody MarketReportRequest request) {
        final boolean result = reportService.submitMarketReportFromRequest(request);
        if (result) {
            log.info("Market {} submit market report {}. Success. ", subject, request.getId());
            return ResponseEntity.ok().build();
        } else {
            log.info("Market {} create new entry in market report {}. Not Complete. ", subject, request.getId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
    }


}
