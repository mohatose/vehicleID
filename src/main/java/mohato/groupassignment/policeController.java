package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.time.LocalDate;
import java.sql.Date;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;

public class  policeController {

    @FXML
    private VBox aCardss;

    @FXML
    private TableColumn<PoliceReport, String>dCole;

    @FXML
    private DatePicker dField;

    @FXML
    private TableColumn<PoliceReport, String>descCole;

    @FXML
    private TextArea descField;

    @FXML
    private ColumnConstraints gridFields;

    @FXML
    private TableColumn<PoliceReport, String> oCole;

    @FXML
    private TextField oField;

    @FXML
    private ProgressBar rBar;

    @FXML
    private VBox rCard;

    @FXML
    private ProgressIndicator rIndi;

    @FXML
    private ScrollPane rScroll;

    @FXML
    private Label ra;

    @FXML
    private TableView<PoliceReport> reportTable;

    @FXML
    private TableColumn<PoliceReport, Integer> reportidCol;

    @FXML
    private TextField reportidField;

    @FXML
    private Label ru;

    @FXML
    private Button savereportBtn;

    @FXML
    private VBox tCardss;

    @FXML
    private Label tr;

    @FXML
    private TableColumn<PoliceReport, String>typeCole;

    @FXML
    private TextField typeField;

    @FXML
    private VBox uCardss;

    @FXML
    private TableColumn<PoliceReport, Integer> vCole;

    @FXML
    private ComboBox<Vehicle> vField;



    @FXML
    public void initialize() {

        reportidCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("reportId"));
        vCole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("vehicleId"));
        dCole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("reportDate"));
        typeCole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("reportType"));
        descCole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("description"));
        oCole.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("officerName"));

        loadVehicles();
        loadReports();
        loadStatistics();
    }
    private void loadReports() {

        var list = javafx.collections.FXCollections.<PoliceReport>observableArrayList();

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {
            String sql = "SELECT * FROM police_report";
            var stmt = conn.prepareStatement(sql);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new PoliceReport(
                        rs.getInt("report_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("report_date"),
                        rs.getString("report_type"),
                        rs.getString("description"),
                        rs.getString("officer_name")
                ));
            }

            reportTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSavereport(ActionEvent event) {

        Vehicle selectedVehicle = vField.getValue();
        LocalDate date = dField.getValue();
        String type = typeField.getText();
        String desc = descField.getText();
        String officer = oField.getText();

        if (selectedVehicle == null){
            addActivity("Fill required fields");
            return;
        }

        if (date == null) {
            addActivity("Select a date");
            return;
        }

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {
            String sql = "INSERT INTO police_report (vehicle_id, report_date, report_type, description, officer_name) VALUES (?, ?, ?, ?, ?)";

            var stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selectedVehicle.getVehicleId());
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setString(3, type);
            stmt.setString(4, desc);
            stmt.setString(5, officer);

            stmt.executeUpdate();

            addActivity("Report saved successfully");

            loadReports();
            loadStatistics();

            // clear fields
            vField.setValue(null);
            dField.setValue(null);
            typeField.clear();
            descField.clear();
            oField.clear();

        } catch (Exception e) {
            addActivity("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadStatistics() {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        var conn = db.getConn();

        try {

            var rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM police_report");
            rs1.next();
            int total = rs1.getInt(1);

            // simple logic: attended = reports with officer_name
            var rs2 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM police_report WHERE officer_name IS NOT NULL");
            rs2.next();
            int attended = rs2.getInt(1);

            int unattended = total - attended;

            tr.setText(String.valueOf(total));
            ra.setText(String.valueOf(attended));
            ru.setText(String.valueOf(unattended));

            double progress = total == 0 ? 0 : (double) attended / total;

            rBar.setProgress(progress);
            rIndi.setProgress(progress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActivity(String msg) {

        Label log = new Label("✔ " + msg);
        log.setStyle("-fx-text-fill: green;");

        rCard.getChildren().add(log);

        rScroll.setVvalue(1.0);
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

            vField.setItems(list); // ✅ THIS connects data to ComboBox

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
