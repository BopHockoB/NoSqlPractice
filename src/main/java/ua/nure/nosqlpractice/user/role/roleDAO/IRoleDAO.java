package ua.nure.nosqlpractice.user.role.roleDAO;

import org.bson.types.ObjectId;
import ua.nure.nosqlpractice.user.role.Role;

import java.util.Set;

public interface IRoleDAO {

    Set<Role> getAllByUserId(ObjectId userId);


}
