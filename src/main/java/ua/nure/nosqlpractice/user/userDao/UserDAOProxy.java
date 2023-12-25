package ua.nure.nosqlpractice.user.userDao;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.observers.Observer;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.role.Role;

import java.util.List;
import java.util.Optional;

//TODO implement abstract class DAOProxy that contains IUser object, checkAuthorities and getUser methods
@Component
@RequiredArgsConstructor
public class UserDAOProxy implements IUserDAO{

    private final IUserDAO userDAO;


    boolean checkAuthorities(Role roleToHave){
        return getCurrentUser().getRoles().contains(roleToHave);

    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return userDAO.getByEmail(authentication.getName()).orElse(null);
        }
        return null;
    }

    @Override
    public void create(User user) {
    if (checkAuthorities(Role.ADMIN))
        userDAO.create(user);
    else
        throw new SecurityException("User does not have required authorities");
    }

    @Override
    public Optional<User> getById(ObjectId id) {
        return userDAO.getById(id);
    }

    @Override
    public Optional<User> getByLastName(String name) {
        return userDAO.getByLastName(name);
    }

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    public void update(User user) {
        if (checkAuthorities(Role.ADMIN))
            userDAO.update(user);
        else
            throw new SecurityException("User does not have required authorities");
    }


    @Override
    public void delete(ObjectId id) {
        if (checkAuthorities(Role.ADMIN))
            userDAO.delete(id);
        else
            throw new SecurityException("User does not have required authorities");
    }


    @Override
    public Optional<User> getByEmail(String email) {
        return userDAO.getByEmail(email);
    }

    public void registerObserver(Observer observer) {
        if (userDAO instanceof UserMySQLDAO)
            ((UserMySQLDAO) userDAO).registerObserver(observer);
    }

    public void removeObserver(Observer observer) {
        if (userDAO instanceof UserMySQLDAO)
            ((UserMySQLDAO) userDAO).removeObserver(observer);
    }

}
