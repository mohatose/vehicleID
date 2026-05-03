package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;


import javafx.scene.control.*;


public class violationController {

    @FXML
    private GridPane GridField;

    @FXML
    private TableColumn<Violation, Double> fineCol;

    @FXML
    private TextField fineamountField;

    @FXML
    private VBox listV;

    @FXML
    private VBox paidCard;

    @FXML
    private Label pf;

    @FXML
    private Button saveviolationBtn;

    @FXML
    private TableColumn<Violation, String> statusCol;

    @FXML
    private ComboBox<String> statusField;

    @FXML
    private VBox totalVcard;

    @FXML
    private Label tv;

    @FXML
    private Label uf;

    @FXML
    private VBox unpaidCard;

    @FXML
    private TableColumn<Violation, Integer> vehicleidCol;

    @FXML
    private ComboBox<Vehicle> vehicleidField;

    @FXML
    private ProgressBar violationBar;

    @FXML
    private ProgressIndicator violationIndi;

    @FXML
    private ScrollPane violationScroll;

    @FXML
    private TableView<Violation> violationTable;

    @FXML
    private TableColumn<Violation, String> violationdateCol;

    @FXML
    private javafx.scene.control.DatePicker violationdateField;

    @FXML
    private TableColumn<Violation, Integer> violationidCol;

    @FXML
    private TextField violationidField;

    @FXML
    private TableColumn<Violation, String> violationtypeCol;

    @FXML
    private TextField violationtypeField;



    @FXML
    public void initialize() {

        violationidCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("violationId"));
        vehicleidCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("vehicleId"));
        violationdateCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("violationDate"));
        violationtypeCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("violationType"));
        fineCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("fineAmount"));
        statusCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        statusField.getItems().addAll("Paid", "Unpaid");

        loadVehicles();
        loadViolations();
        loadStats();
    }

    private void loadViolations() {

        var list = javafx.collections.FXCollections.<Violation>observableArrayList();

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {
            var rs = conn.createStatement().executeQuery("SELECT * FROM violation");

            while (rs.next()) {
                list.add(new Violation(
                        rs.getInt("violation_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("violation_date"),
                        rs.getString("violation_type"),
                        rs.getDouble("fine_amount"),
                        rs.getString("status")
                ));
            }

            violationTable.setItems(list);

        } catch (Exception e) {
            addLog("Failed to load violations");
            showAlert("Load Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleSaveviolation(ActionEvent event) {

        Vehicle selectedVehicle = vehicleidField.getValue();
        LocalDate date = violationdateField.getValue();
        String type = violationtypeField.getText();
        String fine = fineamountField.getText();
        String status = statusField.getValue();

        if (selectedVehicle == null || date == null) {
            addLog("Fill required fields");
            return;
        }

        if (date == null) {
            addLog("Use YYYY-MM-DD format");
            return;
        }

        if (status == null) {
            addLog("Select status");
            return;
        }

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {
            String sql = "INSERT INTO violation (vehicle_id, violation_date, violation_type, fine_amount, status) VALUES (?, ?, ?, ?, ?)";

            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selectedVehicle.getVehicleId());
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setString(3, type);
            stmt.setDouble(4, Double.parseDouble(fine));
            stmt.setString(5, status);

            stmt.executeUpdate();

            addLog("Violation saved");

            loadViolations();
            loadStats();

            vehicleidField.setValue(null);
            violationdateField.setValue(null);
            violationtypeField.clear();
            fineamountField.clear();
            statusField.setValue(null);

        } catch (Exception e) {
            addLog("Save failed");
            showAlert("Database Error", e.getMessage(), Alert.AlertType.ERROR);
            showAlert("Success", "Violation saved successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void loadStats() {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {

            var rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM violation");
            rs1.next();
            int total = rs1.getInt(1);

            var rs2 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM violation WHERE status='Paid'");
            rs2.next();
            int paid = rs2.getInt(1);

            int unpaid = total - paid;

            tv.setText(String.valueOf(total));
            pf.setText(String.valueOf(paid));
            uf.setText(String.valueOf(unpaid));

            double progress = total == 0 ? 0 : (double) paid / total;

            violationBar.setProgress(progress);
            violationIndi.setProgress(progress);

        } catch (Exception e) {
            showAlert("Stats Error", e.getMessage(), Alert.AlertType.WARNING);
        }
    }

    private void addLog(String msg) {

        Label log = new Label("✔ " + msg);
        log.setStyle("-fx-text-fill: #e67e22;");

        listV.getChildren().add(log);

        violationScroll.setVvalue(1.0);
    }
    private void loadVehicleIds() {

        var list = javafx.collections.FXCollections.<Integer>observableArrayList();

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {
            var rs = conn.createStatement().executeQuery("SELECT vehicle_id FROM vehicle");

            while (rs.next()) {
                list.add(rs.getInt("vehicle_id"));
            }



        }catch (Exception e) {
            showAlert("Vehicle Load Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    private void loadVehicles() {

        var list = javafx.collections.FXCollections.<Vehicle>observableArrayList();

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {
            var rs = conn.createStatement().executeQuery(
                    "SELECT vehicle_id, make, registration_number FROM vehicle"
            );

            while (rs.next()) {
                list.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("make"),
                        rs.getString("registration_number")
                ));
            }
            vehicleidField.setItems(list);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message, Alert.AlertType type) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

}
