package fd.se.ooad_project.service;


import fd.se.ooad_project.entity.audit.ProductType;
import fd.se.ooad_project.repository.ProductTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}



