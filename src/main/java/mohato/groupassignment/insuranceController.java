package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;

public class insuranceController {

    @FXML
    private TableView<Insurance> insuranceTable;

    @FXML
    private TableColumn<Insurance, Integer> iCol; // insurance_id

    @FXML
    private TableColumn<Insurance, Integer> vcol; // vehicle_id

    @FXML
    private TableColumn<Insurance, String> pcol; // provider_name

    @FXML
    private TableColumn<Insurance, String> pncol; // policy_number

    @FXML
    private TableColumn<Insurance, String> scol; // start_date

    @FXML
    private TableColumn<Insurance, String> ecol; // end_date

    @FXML
    private TextField endDateField;

    @FXML
    private Label insuredLabel;
    @FXML
    private VBox iCard;

    @FXML
    private Label totalLabel;

    @FXML
    private Label uninsuredLabel;

    @FXML
    private ProgressIndicator iIndi;

    @FXML
    private ScrollPane iScrol;

    @FXML
    private ProgressBar ibar;

    @FXML
    private ComboBox<Integer> vehicleCombo;

    @FXML
    private TextField insuranceIdField;



    @FXML
    private VBox lCArd;

    @FXML
    private TextField policyField;

    @FXML
    private TextField providerField;

    @FXML
    private TextField startDateField;

    @FXML
    private VBox tCard;

    @FXML
    private VBox uCared;


    @FXML
    public void initialize() {

        iCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("insuranceId"));
        vcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("vehicleId"));
        pcol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("providerName"));
        pncol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("policyNumber"));
        scol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("startDate"));
        ecol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("endDate"));
        loadVehicles();
        loadInsuranceData();
        loadStatistics();
        addActivity("System loaded");
    }
    private void loadInsuranceData() {

        javafx.collections.ObservableList<Insurance> list =
                javafx.collections.FXCollections.observableArrayList();

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        java.sql.Connection conn = db.getConn();

        try {
            String sql = "SELECT * FROM insurance";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Insurance(
                        rs.getInt("insurance_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("provider_name"),
                        rs.getString("policy_number"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                ));
            }

            insuranceTable.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handlesaveInsurance(ActionEvent event) {

        Integer vehicleId = vehicleCombo.getValue();;
        String provider = providerField.getText();
        String policy = policyField.getText();
        String start = startDateField.getText();
        String end = endDateField.getText();
        addActivity("Insurance saved for vehicle ID: " + vehicleId);
        addActivity("Error: Fill all fields");
        addActivity("Error: Invalid date format");
        addActivity("Insurance saved successfully", "success");
        addActivity("Invalid date format", "error");
        addActivity("Data loaded", "info");


        if (vehicleId == null || provider.isEmpty() || policy.isEmpty()
                || start.isEmpty() || end.isEmpty()) {
            System.out.println("Fill all fields");
            return;
        }

        if (!start.matches("\\d{4}-\\d{2}-\\d{2}") ||
                !end.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Use date format YYYY-MM-DD");
            return;
        }

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        java.sql.Connection conn = db.getConn();

        if (conn == null) {
            System.out.println("DB connection failed");
            return;
        }

        try {
            String sql = "INSERT INTO insurance (vehicle_id, provider_name, policy_number, start_date, end_date) VALUES (?, ?, ?, ?, ?)";

            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vehicleId);
            stmt.setString(2, provider);
            stmt.setString(3, policy);
            stmt.setDate(4, java.sql.Date.valueOf(start));
            stmt.setDate(5, java.sql.Date.valueOf(end));

            stmt.executeUpdate();

            System.out.println("Saved successfully");

            loadInsuranceData();
            loadStatistics();


            providerField.clear();
            policyField.clear();
            startDateField.clear();
            endDateField.clear();

        } catch (Exception e) {
            addActivity("Database error: " + e.getMessage());
        }

    }

    private void addActivity(String message, String type) {

        Label activity = new Label("✔ " + message);

        switch (type) {
            case "success":
                activity.setStyle("-fx-text-fill: green;");
                break;
            case "error":
                activity.setStyle("-fx-text-fill: red;");
                break;
            case "info":
                activity.setStyle("-fx-text-fill: blue;");
                break;
        }

        lCArd.getChildren().add(activity);
        iScrol.setVvalue(1.0);
    }
    private void loadVehicles() {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        java.sql.Connection conn = db.getConn();

        try {
            String sql = "SELECT vehicle_id FROM vehicle";
            java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                vehicleCombo.getItems().add(rs.getInt("vehicle_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStatistics() {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        java.sql.Connection conn = db.getConn();

        try {

            // TOTAL VEHICLES
            String totalSql = "SELECT COUNT(*) FROM vehicle";
            java.sql.ResultSet rs1 = conn.createStatement().executeQuery(totalSql);
            rs1.next();
            int total = rs1.getInt(1);

            // INSURED VEHICLES
            String insuredSql = "SELECT COUNT(DISTINCT vehicle_id) FROM insurance";
            java.sql.ResultSet rs2 = conn.createStatement().executeQuery(insuredSql);
            rs2.next();
            int insured = rs2.getInt(1);

            // UNINSURED VEHICLES
            int uninsured = total - insured;

            // SET VALUES
            totalLabel.setText(String.valueOf(total));
            insuredLabel.setText(String.valueOf(insured));
            uninsuredLabel.setText(String.valueOf(uninsured));

            // UPDATE PROGRESS
            double progress = total == 0 ? 0 : (double) insured / total;

            ibar.setProgress(progress);
            iIndi.setProgress(progress);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addActivity(String message) {

        Label activity = new Label("✔ " + message);
        activity.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");

        lCArd.getChildren().add(activity);


    }


}
