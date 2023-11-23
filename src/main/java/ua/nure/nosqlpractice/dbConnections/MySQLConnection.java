package ua.nure.nosqlpractice.dbConnections;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {


    private final static String DB_HOST = "localhost";

    private final static String DB_PORT = "3306";

    private final static String DB_USER = "root";

    private final static String DB_PASS = "Vanilka125";

    private final static String DB_NAME = "ticket_master";

    private static Connection connection;

    private MySQLConnection() {}

    public static Connection getDBSqlConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String connectionString = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT +
                    "/" + DB_NAME;
            connection = DriverManager.getConnection(connectionString, DB_USER, DB_PASS);
        }
        return connection;
    }
}