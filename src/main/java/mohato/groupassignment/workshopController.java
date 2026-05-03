package mohato.groupassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class workshopController {

    @FXML
    private TextField vehicleIdField;

    @FXML
    private TextField regNumberField;

    @FXML
    private TextField makeField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField yearField;

    @FXML
    private ComboBox<Integer> ownerIdField;

    @FXML
    private TableView<Vehicle> workshopTable;

    @FXML
    private TableColumn<Vehicle, Integer> vehicleWoCol;

    @FXML
    private TableColumn<Vehicle, String> regWoCol;

    @FXML
    private TableColumn<Vehicle, String> makeWoCol;

    @FXML
    private TableColumn<Vehicle, String> modelWoCol;

    @FXML
    private TableColumn<Vehicle, Integer> yearWocol;

    @FXML
    private TableColumn<Vehicle, Integer> ownerIdWoCol;

    @FXML
    private Label totalV;

    @FXML
    private Label regV;

    @FXML
    private Label unreV;

    @FXML
    private ScrollPane vehicleScroll;

    @FXML
    private VBox actvityV;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        vehicleWoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getVehicleId()).asObject());

        regWoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getRegistrationNumber()));

        makeWoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getMake()));

        modelWoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getModel()));

        yearWocol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getYear()).asObject());

        ownerIdWoCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getOwnerId() == null ? 0 : data.getValue().getOwnerId()
                ).asObject());

        log("Workshop initialized");
        loadOwners();
        loadVehicles();
    }


    private void loadVehicles() {

        vehicleList.clear();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            log("Loading vehicles...");

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM vehicle");

            while (rs.next()) {

                vehicleList.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("registration_number"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        (Integer) rs.getObject("owner_id")
                ));
            }

            workshopTable.setItems(vehicleList);

            log(vehicleList.size() + " vehicles loaded");

            updateStats();

        } catch (Exception e) {
            log("ERROR loading vehicles: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    void handleaddVehicle(ActionEvent event) {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            log("Adding vehicle: " + regNumberField.getText());

            String sql = "INSERT INTO vehicle (registration_number, make, model, year, owner_id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, regNumberField.getText());
            ps.setString(2, makeField.getText());
            ps.setString(3, modelField.getText());
            ps.setInt(4, Integer.parseInt(yearField.getText()));

            Integer ownerId = ownerIdField.getValue();

            if (ownerId == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, ownerId);
            }

            ps.executeUpdate();

            log("Vehicle added successfully");

            clearFields();
            loadVehicles();

        } catch (Exception e) {
            log("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void clearFields() {
        vehicleIdField.clear();
        regNumberField.clear();
        makeField.clear();
        modelField.clear();
        yearField.clear();

        ownerIdField.setValue(null); // ✅ correct way for ComboBox
    }

    private void updateStats() {

        int total = vehicleList.size();

        int registered = (int) vehicleList.stream()
                .filter(v -> v.getRegistrationNumber() != null && !v.getRegistrationNumber().isEmpty())
                .count();

        int unregistered = total - registered;

        totalV.setText(String.valueOf(total));
        regV.setText(String.valueOf(registered));
        unreV.setText(String.valueOf(unregistered));

        log("Stats updated");
    }
    private void log(String msg) {

        if (actvityV == null) return;

        Label log = new Label("• " + msg);
        log.setStyle("-fx-text-fill:#2c3e50; -fx-font-size:12px;");

        actvityV.getChildren().add(log);

        vehicleScroll.layout();
        vehicleScroll.setVvalue(1.0);
    }
    private void loadOwners() {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT customer_id FROM customer"
            );

            while (rs.next()) {
                ownerIdField.getItems().add(rs.getInt("customer_id"));
            }

        } catch (Exception e) {
            log("ERROR loading owners: " + e.getMessage());
            e.printStackTrace();
        }
    }
}