package fd.se.ooad_project.interceptor;

import fd.se.ooad_project.entity.usr.User;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Data
public class Subject {

    private User user;

    @Override
    public String toString() {
        return user.getName();
    }
}
