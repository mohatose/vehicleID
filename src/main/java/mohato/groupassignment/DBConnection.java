package mohato.groupassignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private Connection conn;
    private final String url = "jdbc:postgresql://localhost:5432/vehicle_system";
    private String user;
    private String password;

    public DBConnection(String user, String password) {
        this.user = user;
        this.password = password;

        openConn();
    }

    public void openConn() {
        try {
           
            Class.forName("org.postgresql.Driver");

            this.conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to PostgreSQL!");

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found!");
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }
}