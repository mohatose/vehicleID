package mohato.groupassignment;

import mohato.groupassignment.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class adminController {

    @FXML private TableView<UserModel> userTable;

    @FXML private TableColumn<UserModel, Integer> colId;
    @FXML private TableColumn<UserModel, String> colName;
    @FXML private TableColumn<UserModel, String> colRole;
    @FXML private TableColumn<UserModel, String> colStatus;
    @FXML private TableColumn<UserModel, String> colEmail;

    @FXML private TextField searchField;

    private final ObservableList<UserModel> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        userTable.setItems(list);

        loadUsers();
    }

    // ================= LOAD USERS =================
    public void loadUsers() {

        list.clear();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "SELECT * FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                User user = UserFactory.createUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("status")
                );

                list.add(new UserModel(
                        user.getId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getEmail(),
                        user.getStatus()
                ));

                System.out.println(user.dashboardAccess());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= OPEN POPUP =================
    @FXML
    public void openAddUserForm() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mohato/groupassignment/adduser.fxml")
            );

            Parent root = loader.load();

            // GET CONTROLLER OF POPUP
            adduserController controller = loader.getController();
            controller.setAdminController(this);

            Stage stage = new Stage();
            stage.setTitle("Add New User");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ADD USER (FIXED - REMOVE EMPTY VALUES) =================
    @FXML
    void handleAddUser(ActionEvent event) {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "INSERT INTO users(username,password,role,email,status) VALUES(?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, "newuser");
            stmt.setString(2, "1234");
            stmt.setString(3, "customer");
            stmt.setString(4, "test@email.com");
            stmt.setString(5, "ACTIVE");

            stmt.executeUpdate();

            loadUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    @FXML
    void handleDeleteUser(ActionEvent event) {

        UserModel selected = userTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            System.out.println("Select user first");
            return;
        }

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "DELETE FROM users WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getId());

            stmt.executeUpdate();

            loadUsers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SEARCH =================
    @FXML
    void handleSearch(ActionEvent event) {

        list.clear();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "SELECT * FROM users WHERE username ILIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + searchField.getText() + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                list.add(new UserModel(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private UserModel selectedUser;
    // ================= EDIT =================
    @FXML
    void handleEditUser(ActionEvent event) {

        selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            System.out.println("Select a user first");
            return;
        }

        openEditUserForm();
    }
    public void openEditUserForm() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/mohato/groupassignment/adduser.fxml")
            );

            Parent root = loader.load();

            adduserController controller = loader.getController();

            // send selected user to popup
            controller.setEditMode(selectedUser, this);

            Stage stage = new Stage();
            stage.setTitle("Edit User");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void insertActivity(String message) {

        String sql = "INSERT INTO activity_log(message) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/vehicle_system",
                "postgres",
                "ntj@nalanga#2$8"
        );
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, message);
            ps.executeUpdate();

            System.out.println("Activity inserted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}