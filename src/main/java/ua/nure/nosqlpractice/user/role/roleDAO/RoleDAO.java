package ua.nure.nosqlpractice.user.role.roleDAO;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ua.nure.nosqlpractice.dbConnections.MySQLConnection;
import ua.nure.nosqlpractice.user.role.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class RoleDAO implements IRoleDAO{

    private final Connection connection;

    public RoleDAO() throws SQLException {
        this.connection = MySQLConnection.getDBSqlConnection();
    }


    @Override
    public Set<Role> getAllByUserId(ObjectId userId) {
        String query = "SELECT r.role_name FROM user_has_role AS ur JOIN role AS r ON ur.role_id = r.role_id WHERE user_id = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userId.toHexString());
                try (ResultSet resultSet = statement.executeQuery()){
                    Set<Role> roles = new HashSet<>();
                    while (resultSet.next()){
                         roles.add(Role.valueOf(
                                 resultSet.getString("role_name"))
                         );
                    }
                    return roles;
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
