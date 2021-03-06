package fd.se.ooad_project.service;

import fd.se.ooad_project.entity.audit.AuditTask;
import fd.se.ooad_project.entity.consts.Role;
import fd.se.ooad_project.entity.usr.User;
import fd.se.ooad_project.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public boolean createUser(String name, Role role) {
        User user = userRepository.findByName(name);
        if (user != null) {
            return false;
        }
        user = User.of(name, role);
        userRepository.save(user);
        return true;
    }

    public boolean existUser(String name) {
        return null != userRepository.findByName(name);
    }

    public User getUser(String name) {
        return userRepository.findByName(name);
    }

    public List<User> getUncompletedMarketsInTask(AuditTask task) {
        return userRepository.findUncompletedMarketsInTask(task);
    }

    public List<User> getUsers(Role role) {
        return userRepository.findByRole(role);
    }


    public int gradesOf(User user) {
        return userRepository.gradesForUser(user);
    }


}
