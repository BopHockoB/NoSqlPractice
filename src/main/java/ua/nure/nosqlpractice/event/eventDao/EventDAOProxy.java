package ua.nure.nosqlpractice.event.eventDao;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.event.Event;
import ua.nure.nosqlpractice.observers.Observer;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.role.Role;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EventDAOProxy implements IEventDAO{


    private final IUserDAO userDAO;
    private final IEventDAO eventDAO;


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
    public void create(Event event) {
        if (checkAuthorities(Role.ADMIN))
            eventDAO.create(event);
        else
            throw new SecurityException("User does not have required authorities");
    }


    @Override
    public Optional<Event> getById(ObjectId id) {
        return eventDAO.getById(id);
    }

    @Override
    public Optional<Event> getByName(String name) {
        return eventDAO.getByName(name);
    }

    @Override
    public List<Event> getAll() {
        return eventDAO.getAll();
    }

    @Override
    public void update(Event event) {
        if (checkAuthorities(Role.ADMIN))
            eventDAO.update(event);
        else
            throw new SecurityException("User does not have required authorities");
    }

    @Override
    public void delete(ObjectId id) {
        if (checkAuthorities(Role.ADMIN))
            eventDAO.delete(id);
        else
            throw new SecurityException("User does not have required authorities");

    }

    public void registerObserver(Observer observer) {
        if (eventDAO instanceof EventMySQLDAO)
            ((EventMySQLDAO)eventDAO).registerObserver(observer);
    }

    public void removeObserver(Observer observer) {
        if (eventDAO instanceof EventMySQLDAO)
            ((EventMySQLDAO)eventDAO).removeObserver(observer);
    }

}
