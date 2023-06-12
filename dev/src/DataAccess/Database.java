package DataAccess;

import java.sql.*;
public class Database {
    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\sasmo\\Documents\\ADSS_Group_H\\identifier.sqlite";
    public static Connection connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(DB_URL);
    }
}
