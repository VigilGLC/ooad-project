package fd.se.ooad_project.pojo.request;

import fd.se.ooad_project.entity.consts.Role;
import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private Role role;
}
