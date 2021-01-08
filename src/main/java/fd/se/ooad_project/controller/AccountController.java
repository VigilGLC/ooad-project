package fd.se.ooad_project.controller;


import fd.se.ooad_project.pojo.request.MereNameRequest;
import fd.se.ooad_project.pojo.request.SignUpRequest;
import fd.se.ooad_project.pojo.response.DateResponse;
import fd.se.ooad_project.service.ProductService;
import fd.se.ooad_project.service.UserService;
import fd.se.ooad_project.service.date.IDateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Slf4j
public class AccountController {

    private final ProductService productService;
    private final UserService userService;
    private final IDateService dateService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signIn(@RequestBody SignUpRequest request) {
        final boolean created =
                userService.createUser(request.getName(), request.getRole());
        if (created) {
            log.info("{} sign up Success. ", request.getName());
            return ResponseEntity.ok().build();
        } else {
            log.info("{} sign up Failed. ", request.getName());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody MereNameRequest request) {
        final String name = request.getName();
        final boolean exists = userService.existUser(name);
        if (exists) {
            log.info("{} sign in Success. ", name);
            return ResponseEntity.ok(userService.getUser(name));
        } else {
            log.info("{} sign in Failed. ", name);
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/date")
    public ResponseEntity<?> getDate() {
        return ResponseEntity.ok(DateResponse.of(dateService.currDate()));
    }

    @GetMapping("/productTypes")
    public ResponseEntity<?> allProductTypes() {
        return ResponseEntity.ok(productService.getAll());
    }
}
