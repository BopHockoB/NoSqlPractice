package ua.nure.nosqlpractice.dbConnections;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MySQLConnectionTest {

    @Test
    void getDBSqlConnection() {
        try {

            Connection connection = MySQLConnection.getDBSqlConnection();

            assertNotNull(connection);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}