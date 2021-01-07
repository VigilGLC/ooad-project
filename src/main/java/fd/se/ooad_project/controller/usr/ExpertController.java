package fd.se.ooad_project.controller.usr;


import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.interceptor.authorize.Authorized;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/expert")
@AllArgsConstructor
@Slf4j
@Authorized(role = Role.ANY)
public class ExpertController {
}
