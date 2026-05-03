package mohato.groupassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.scene.layout.VBox;

public class servicerecordController {

    @FXML private TextField costField;
    @FXML private TextArea destypeField;

    @FXML private TextField serviceidField;
    @FXML private TextField stype;

    @FXML private DatePicker sdateField;

    @FXML private ComboBox<Integer> vidField;

    @FXML private TableView<ServiceRecord> serveTable;

    @FXML private TableColumn<ServiceRecord, Integer> serveidCol;
    @FXML private TableColumn<ServiceRecord, Integer> vehiidCol;
    @FXML private TableColumn<ServiceRecord, String> servedateCol;
    @FXML private TableColumn<ServiceRecord, String> servetypeCol;
    @FXML private TableColumn<ServiceRecord, String> descripCol;
    @FXML private TableColumn<ServiceRecord, Double> costsCol;

    @FXML private Label totas;
    @FXML private Label pse;
    @FXML private Label ups;

    @FXML private ProgressBar serveBar;
    @FXML private ProgressIndicator serveIndi;

    @FXML private VBox vact;
    @FXML private ScrollPane serveScroll;

    @FXML
    public void initialize() {

        serveidCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getServiceId()
                ).asObject()
        );

        vehiidCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getVehicleId()
                ).asObject()
        );

        servedateCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getServiceDate()
                )
        );

        servetypeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getServiceType()
                )
        );

        descripCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDescription()
                )
        );

        costsCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getCost()
                ).asObject()
        );
        loadVehicleIds();
        loadServices();
    }

    @FXML
    void handleseveservice(ActionEvent event) {

        try {
            if (vidField.getValue() == null || sdateField.getValue() == null) {
                showAlert("Validation Error", "Please select Vehicle ID and Date");
                return;
            }

            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql =
                    "INSERT INTO service_record (vehicle_id, service_date, service_type, description, cost) " +
                            "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, vidField.getValue());

            LocalDate date = sdateField.getValue();
            ps.setDate(2, java.sql.Date.valueOf(date));

            ps.setString(3, stype.getText());
            ps.setString(4, destypeField.getText());
            ps.setDouble(5, Double.parseDouble(costField.getText()));

            ps.executeUpdate();

            addActivity("Service record saved successfully");

            clearFields();
            loadServices();

        } catch (Exception e) {
            showAlert("Database Error", e.getMessage());
            addActivity("Error saving service record");
        }
    }

    private void loadServices() {

        ObservableList<ServiceRecord> list = FXCollections.observableArrayList();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM service_record");

            while (rs.next()) {

                list.add(new ServiceRecord(
                        rs.getInt("service_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDate("service_date").toString(),
                        rs.getString("service_type"),
                        rs.getString("description"),
                        rs.getDouble("cost")
                ));
            }

            serveTable.setItems(list);
            updateStats(list);

        } catch (Exception e) {
            showAlert("Load Error", e.getMessage());
        }
    }

    private void clearFields() {
        serviceidField.clear();
        vidField.setValue(null);
        sdateField.setValue(null);
        stype.clear();
        destypeField.clear();
        costField.clear();
    }

    private void addActivity(String msg) {
        Label log = new Label("• " + msg);
        log.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");
        vact.getChildren().add(log);
        serveScroll.setVvalue(1.0);
    }

    private void updateStats(ObservableList<ServiceRecord> list) {

        int total = list.size();

        long paid = list.stream()
                .filter(s -> s.getCost() > 0)
                .count();

        long unpaid = total - paid;

        totas.setText(String.valueOf(total));
        pse.setText(String.valueOf(paid));
        ups.setText(String.valueOf(unpaid));

        double progress = total == 0 ? 0 : (double) paid / total;

        serveBar.setProgress(progress);
        serveIndi.setProgress(progress);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void loadVehicleIds() {

        ObservableList<Integer> ids = FXCollections.observableArrayList();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "SELECT vehicle_id FROM vehicle ORDER BY vehicle_id";

            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                ids.add(rs.getInt("vehicle_id"));
            }

            vidField.setItems(ids);

        } catch (Exception e) {
            e.printStackTrace();
            addActivity("Failed to load vehicle IDs");
        }
    }
}