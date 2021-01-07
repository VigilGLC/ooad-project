package fd.se.ooad_project.repository.audittask;


import fd.se.ooad_project.entity.audit.ExpertTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertTaskRepository extends CrudRepository<ExpertTask,Integer> {
}
