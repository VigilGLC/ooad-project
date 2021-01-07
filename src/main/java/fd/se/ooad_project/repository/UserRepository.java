package fd.se.ooad_project.repository;

import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    User findByName(String name);

    List<User> findByRoleAndNameIn(Role role, List<String> names);
}
