package fd.se.ooad_project.interceptor.authorize;

import fd.se.ooad_project.entity.consts.Role;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorized {
    Role role() default Role.ANY;
}
