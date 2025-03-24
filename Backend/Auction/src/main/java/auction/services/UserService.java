package auction.services;

import auction.entities.RO.UserRO;
import auction.entities.User;
import auction.entities.enums.Role;
import auction.entities.utils.MessageUtils;
import auction.exceptions.ServiceException;
import auction.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        try {
            List<User> users = userRepository.findAll();
            log.info(MessageUtils.retrieveSuccess("Users"));
            return users;
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.retrieveError("Users"), e);
        }
    }

    public List<User> getAllByFilter(Role role) {
        try {
            return userRepository.findAllByRole(role);
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.retrieveError("Filtered Users"), e);
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ServiceException(MessageUtils.notFound("User"), new RuntimeException("User not found")));
    }

    @Transactional
    public void save(UserRO userRO) {
        try {
            User user = userRO.toEntity();
            userRepository.save(user);
            log.info(MessageUtils.saveSuccess("User"));
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.saveError("User"), e);
        }
    }

    @Transactional
    public void update(Long id, UserRO userRO) {
        try {
            User existingUser = getUserById(id);
            existingUser.updateFromRO(userRO);
            userRepository.save(existingUser);
            log.info(MessageUtils.updateSuccess("User"));
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.updateError("User"), e);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            User user = getUserById(id);
            userRepository.delete(user);
            log.info(MessageUtils.deleteSuccess("User"));
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.deleteError("User"), e);
        }
    }
}
