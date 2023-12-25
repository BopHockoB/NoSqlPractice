package ua.nure.nosqlpractice.customerTicket.customerTicketDao;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.customerTicket.CustomerTicket;
import ua.nure.nosqlpractice.observers.Observer;
import ua.nure.nosqlpractice.user.User;
import ua.nure.nosqlpractice.user.role.Role;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerTicketDAOProxy implements ICustomerTicketDAO{

    private final IUserDAO userDAO;
    private final ICustomerTicketDAO customerTicketDAO;

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
    public void create(CustomerTicket customerTicket) {
    if (checkAuthorities(Role.ADMIN))
        customerTicketDAO.create(customerTicket);
    else
        throw new SecurityException("User does not have required authorities");
    }

    @Override
    public Optional<CustomerTicket> getById(ObjectId id) {
        return customerTicketDAO.getById(id);
    }

    @Override
    public List<CustomerTicket> getAll() {
        return customerTicketDAO.getAll();
    }

    @Override
    public void update(CustomerTicket customerTicket) {
        if (checkAuthorities(Role.ADMIN))
            customerTicketDAO.update(customerTicket);
        else
            throw new SecurityException("User does not have required authorities");
    }

    @Override
    public void delete(ObjectId id) {
        if (checkAuthorities(Role.ADMIN))
            customerTicketDAO.delete(id);
        else
            throw new SecurityException("User does not have required authorities");
    }

    public void registerObserver(Observer observer) {
        if (customerTicketDAO instanceof CustomerTicketMySQLDAO)
            ((CustomerTicketMySQLDAO) customerTicketDAO).registerObserver(observer);
    }

    public void removeObserver(Observer observer) {
        if (customerTicketDAO instanceof CustomerTicketMySQLDAO)
            ((CustomerTicketMySQLDAO) customerTicketDAO).removeObserver(observer);
    }
}
