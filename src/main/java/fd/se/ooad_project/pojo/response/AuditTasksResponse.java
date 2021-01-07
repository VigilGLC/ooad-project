package fd.se.ooad_project.pojo.response;


import fd.se.ooad_project.entity.audit.AuditTask;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor(staticName = "newResponse")
public class AuditTasksResponse {

    private List<AuditTask> tasks = new ArrayList<>();

    public void add(Collection<? extends AuditTask> tasks) {
        this.tasks.addAll(tasks);
    }

}
