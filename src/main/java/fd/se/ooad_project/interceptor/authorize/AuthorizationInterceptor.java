package fd.se.ooad_project.interceptor.authorize;

import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.interceptor.Subject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@AllArgsConstructor
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final Subject subject;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        Authorized annotation;
        if (null == (annotation = method.getAnnotation(Authorized.class))) {
            annotation = method.getDeclaringClass().getAnnotation(Authorized.class);
        }
        if (annotation != null) {
            final Role role = annotation.role();
            final User currUser = subject.getUser();
            if (currUser == null || (role != Role.ANY && currUser.getRole() != role)) {
                if (currUser != null) {
                    log.warn("User {} authority Intercepted.", currUser.getName());
                }
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}

