package mohato.groupassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class adduserController {

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private ComboBox<String> roleBox;
    @FXML private TextField username;
    private UserModel editingUser;
    private adminController adminController;
    private boolean editMode = false;


    public void setAdminController(adminController adminController) {
        this.adminController = adminController;
    }

    @FXML
    public void initialize() {
        roleBox.getItems().addAll("admin", "insurance", "customer", "police", "workshop");
    }

    @FXML
    public void handlesave() {

        try {
            DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
            Connection conn = db.getConn();

            if (editMode) {

                String sql = "UPDATE users SET username=?, role=?, email=? WHERE id=?";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username.getText());
                stmt.setString(2, roleBox.getValue());
                stmt.setString(3, email.getText());
                stmt.setInt(4, editingUser.getId());

                stmt.executeUpdate();

            } else {

                String sql = "INSERT INTO users(username,password,role,email,status) VALUES(?,?,?,?,?)";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username.getText());
                stmt.setString(2, password.getText());
                stmt.setString(3, roleBox.getValue());
                stmt.setString(4, email.getText());
                stmt.setString(5, "ACTIVE");

                stmt.executeUpdate();
            }

            // refresh table
            adminController.loadUsers();

            // close popup
            username.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setEditMode(UserModel user, adminController controller) {

        this.editingUser = user;
        this.adminController = controller;
        this.editMode = true;

        username.setText(user.getUsername());
        email.setText(user.getEmail());
        roleBox.setValue(user.getRole());
    }
}