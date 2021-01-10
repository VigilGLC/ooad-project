package fd.se.ooad_project.pojo.request;


import fd.se.ooad_project.entity.consts.AuditTaskType;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AuditTaskInitiateRequest {

    private AuditTaskType auditTaskType;
    private String expert;

    private String description;

    private LocalDate deadline;

    private List<String> productTypes = new ArrayList<>();

    private List<String> markets;

}
