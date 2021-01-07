package fd.se.ooad_project.service;


import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.repository.ProductTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@AllArgsConstructor
public class ProductService {

    private final ProductTypeRepository productTypeRepository;


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

    public int numberOfUnqualifiedFromEntries(ProductType productType, LocalDate from, LocalDate to) {
        return productTypeRepository.sumUnqualifiedBetween(productType, from, to);
    }

}



