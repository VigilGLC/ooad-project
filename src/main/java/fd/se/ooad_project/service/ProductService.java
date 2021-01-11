package fd.se.ooad_project.service;


import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.repository.ProductTypeRepository;
import fd.se.ooad_project.repository.report.ProductInspectEntryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductInspectEntryRepository entryRepository;

    public boolean createProductType(String name) {
        if (null == productTypeRepository.findByName(name)) {
            final ProductType productType = ProductType.of(name);
            productTypeRepository.save(productType);
            return true;
        }
        return false;
    }

    public ProductType getByName(String typeName) {
        return productTypeRepository.findByName(typeName);
    }

    public List<ProductType> getAll() {
        return productTypeRepository.findAll();
    }

    public int getNumberOfUnqualifiedFromEntries(ProductType productType, LocalDate from, LocalDate to) {
        return entryRepository.sumUnqualifiedBetween(productType, from, to);
    }

    @Transactional
    public List<ProductType> getProductTypesInTask(AuditTask task, boolean completed) {
        final List<ProductType> uncompletedTypes = productTypeRepository.findUncompletedProductTypesInTask(task);
        if (!completed) {
            return uncompletedTypes;
        } else {
            final HashSet<ProductType> types = new HashSet<>(task.getProductTypes());
            types.removeAll(uncompletedTypes);
            return new ArrayList<>(types);
        }
    }

    public int getNumberOfUnqualifiedFromEntriesInTask(ProductType productType, AuditTask task) {
        return entryRepository.sumUnqualifiedInTask(productType, task);
    }

}



