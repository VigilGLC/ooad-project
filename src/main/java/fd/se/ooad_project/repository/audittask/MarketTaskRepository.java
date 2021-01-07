package fd.se.ooad_project.repository.audittask;

import fd.se.ooad_project.entity.audit.MarketTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketTaskRepository extends CrudRepository<MarketTask, Integer> {
}
