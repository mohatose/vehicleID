package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class loginController {

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void handleLogin(ActionEvent event) {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("⚠ Please fill all fields");
            return;
        }

        // 👉 CREATE OBJECT (this is what was missing)
        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        Connection conn = db.getConn();

        if (conn == null) {
            messageLabel.setText("❌ Connection failed");
            return;
        }

        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String role = rs.getString("role");



                javafx.stage.Stage stage =
                        (javafx.stage.Stage) usernameField.getScene().getWindow();

                if (role.equalsIgnoreCase("insurance")) {

                    messageLabel.setText("✅ Insurance Login Successful!");

                    javafx.fxml.FXMLLoader loader =
                            new javafx.fxml.FXMLLoader(getClass().getResource("maininsurance.fxml"));

                    javafx.scene.Parent root = loader.load();

                    stage.setScene(new javafx.scene.Scene(root));
                    stage.setTitle("Insurance System");
                    stage.show();

                }
                if (role.equalsIgnoreCase("police")) {

                    messageLabel.setText("✅ Police Login Successful!");

                    javafx.fxml.FXMLLoader loader =
                            new javafx.fxml.FXMLLoader(getClass().getResource("mainpolice.fxml"));

                    javafx.scene.Parent root = loader.load();

                    stage.setScene(new javafx.scene.Scene(root));
                    stage.setTitle("police System");
                    stage.show();

                }
                if (role.equalsIgnoreCase("workshop")) {

                    messageLabel.setText("✅ workshop Login Successful!");

                    javafx.fxml.FXMLLoader loader =
                            new javafx.fxml.FXMLLoader(getClass().getResource("mainworkshop.fxml"));

                    javafx.scene.Parent root = loader.load();

                    stage.setScene(new javafx.scene.Scene(root));
                    stage.setTitle("workshop System");
                    stage.show();

                }
                if (role.equalsIgnoreCase("ADMIN")) {

                    messageLabel.setText("✅ admin Login Successful!");

                    javafx.fxml.FXMLLoader loader =
                            new javafx.fxml.FXMLLoader(getClass().getResource("mainadmin.fxml"));

                    javafx.scene.Parent root = loader.load();

                    stage.setScene(new javafx.scene.Scene(root));
                    stage.setTitle("admin System");
                    stage.show();

                }else if (role.equalsIgnoreCase("customer")) {

                    messageLabel.setText("✅ customer Login Successful!");

                    javafx.fxml.FXMLLoader loader =
                            new javafx.fxml.FXMLLoader(getClass().getResource("maincustomer.fxml"));

                    javafx.scene.Parent root = loader.load();

                    stage.setScene(new javafx.scene.Scene(root));
                    stage.setTitle("Customer System");
                    stage.show();

                } else {
                    messageLabel.setText("❌ Unknown role");
                }

            } else {
                messageLabel.setText("❌ Invalid username or password");
            }

        } catch (Exception e) {
            messageLabel.setText("❌ Database error");
            e.printStackTrace();
        }
    }
}