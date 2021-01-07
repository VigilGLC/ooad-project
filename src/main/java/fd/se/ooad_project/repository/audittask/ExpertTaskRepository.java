package fd.se.ooad_project.repository.audittask;


import fd.se.ooad_project.entity.audit.ExpertTask;
import fd.se.ooad_project.entity.audit.MarketTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertTaskRepository extends CrudRepository<ExpertTask,Integer> {

    ExpertTask findById(int id);

    List<ExpertTask> findByCompleted(boolean completed);

}
