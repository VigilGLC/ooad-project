package fd.se.ooad_project.repository;

import fd.se.ooad_project.entity.audit.MarketInspectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketInspectTaskRepository extends CrudRepository<MarketInspectTask,Integer> {
}
