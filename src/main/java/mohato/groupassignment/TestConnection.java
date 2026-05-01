package mohato.groupassignment;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        Connection conn = db.getConn();   // ✅ correct method

        if (conn != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}