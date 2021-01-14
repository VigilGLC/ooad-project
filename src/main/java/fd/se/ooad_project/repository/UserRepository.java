package fd.se.ooad_project.repository;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByName(String name);

    List<User> findByRoleAndNameIn(Role role, List<String> names);


    @Query(value =
            "select distinct report.market " +
                    "from MarketReport report " +
                    "where " +
                    "   report.task=:task " +
                    "   and " +
                    "   report.submitted=false "
    )
    List<User> findUncompletedMarketsInTask(AuditTask task);

    List<User> findByRole(Role role);


    @Query(value =
            "select coalesce(sum(record.grading),0) " +
                    "from GradeRecord record " +
                    "where " +
                    "   record.user=:user "
    )
    int gradesForUser(User user);


}
