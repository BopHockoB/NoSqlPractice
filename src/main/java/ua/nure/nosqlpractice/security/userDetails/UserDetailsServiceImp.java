package ua.nure.nosqlpractice.security.userDetails;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.nure.nosqlpractice.user.userDao.IUserDAO;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final IUserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDAO.getByEmail(email)
                .map(UserDetailImp::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}