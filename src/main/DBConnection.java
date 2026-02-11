package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a database connection to the CompetitionDB MySQL database.
 */
public class DBConnection {
	/** Database connection URL */
    private static final String URL = "jdbc:mysql://localhost:3306/CompetitionDB";
    /** Database userName */
    private static final String USER = "root";
    /** Database password */
    private static final String PASSWORD = "";

    /**
     * Establishes and returns a database connection.
     *
     * @return a Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
 