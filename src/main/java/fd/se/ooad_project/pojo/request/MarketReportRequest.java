package fd.se.ooad_project.pojo.request;

import fd.se.ooad_project.entity.report.ProductInspectEntry;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class MarketReportRequest extends MereIdRequest {

    public static class ProductInspectEntrySlim {
        private String type;
        private int unqualified;
    }

    private List<ProductInspectEntrySlim> entrySlims;


    public void update(List<ProductInspectEntry> entries, LocalDate currDate) {
        final Map<String, Integer> typeMap = this.entrySlims.stream().
                collect(Collectors.toMap(e -> e.type, e -> e.unqualified));
        entries.forEach(entry -> {
            entry.setUnqualified(typeMap.get(entry.getType().getName()));
            entry.setArchived(true);
            entry.setDateArchived(currDate);
        });
    }


}
