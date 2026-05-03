package mohato.groupassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;

public class customerqueryController {

    @FXML
    private TableView<CustomerQuery> queryTable;

    @FXML
    private TableColumn<CustomerQuery, Integer> colQueryId;

    @FXML
    private TableColumn<CustomerQuery, Integer> colCustomerId1;

    @FXML
    private TableColumn<CustomerQuery, Integer> colVehicleId;

    @FXML
    private TableColumn<CustomerQuery, String> colDate;

    @FXML
    private TableColumn<CustomerQuery, String> colQueryText;

    @FXML
    private TableColumn<CustomerQuery, String> colResponse;

    @FXML
    private ComboBox<Integer> customerIdField;

    @FXML
    private ComboBox<Integer> vehicleIdField;

    @FXML
    private DatePicker queryDateField;

    @FXML
    private TextArea queryTextArea;

    @FXML
    private Label tq;

    @FXML
    private Label res;

    @FXML
    private Label pen;

    @FXML
    private ProgressBar rBar;

    @FXML
    private ProgressIndicator rIndi;

    @FXML
    private VBox querl;

    @FXML
    private ScrollPane qScroll;

    @FXML
    private VBox pCard;

    @FXML
    private VBox rCard;

    @FXML
    private VBox ttCard;

    // ================= INIT =================
    @FXML
    public void initialize() {
        setupTable();
        loadCombos();
        loadQueries();
    }

    // ================= TABLE =================
    private void setupTable() {

        colQueryId.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getQueryId()).asObject());

        colCustomerId1.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getCustomerId()).asObject());

        colVehicleId.setCellValueFactory(d ->
                new javafx.beans.property.SimpleIntegerProperty(d.getValue().getVehicleId()).asObject());

        colDate.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getQueryDate()));

        colQueryText.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getQueryText()));

        colResponse.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getResponseText()));
    }

    // ================= LOAD COMBOS =================
    private void loadCombos() {
        try (Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn()) {

            ResultSet rs1 = conn.createStatement().executeQuery("SELECT customer_id FROM customer");
            while (rs1.next()) {
                customerIdField.getItems().add(rs1.getInt(1));
            }

            ResultSet rs2 = conn.createStatement().executeQuery("SELECT vehicle_id FROM vehicle");
            while (rs2.next()) {
                vehicleIdField.getItems().add(rs2.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= INSERT QUERY =================
    @FXML
    void handlesendquery(ActionEvent event) {

        try (Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn()) {

            Integer customerId = customerIdField.getValue();
            Integer vehicleId = vehicleIdField.getValue();
            String queryText = queryTextArea.getText();

            LocalDate date = queryDateField.getValue();

            if (customerId == null || vehicleId == null || queryText.isEmpty() || date == null) {
                showAlert("Please fill all fields!");
                return;
            }

            String sql = """
                INSERT INTO customer_query (customer_id, vehicle_id, query_date, query_text)
                VALUES (?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, customerId);
            ps.setInt(2, vehicleId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(4, queryText);

            ps.executeUpdate();

            ActivityLogger.log("New query submitted by customer " + customerId);

            clearForm();
            loadQueries();

        } catch (Exception e) {
            e.printStackTrace();
            ActivityLogger.log("Error submitting query");
        }
    }

    // ================= LOAD TABLE =================
    private void loadQueries() {

        ObservableList<CustomerQuery> list = FXCollections.observableArrayList();

        try (Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn()) {

            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT * FROM customer_query ORDER BY query_id DESC"
            );

            while (rs.next()) {
                list.add(new CustomerQuery(
                        rs.getInt("query_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        String.valueOf(rs.getTimestamp("query_date")),
                        rs.getString("query_text"),
                        rs.getString("response_text")
                ));
            }

            queryTable.setItems(list);
            updateStats(list);
            loadQueriesToVBox(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= STATS =================
    private void updateStats(java.util.List<CustomerQuery> list) {

        int total = list.size();

        long resolved = list.stream()
                .filter(q -> q.getResponseText() != null && !q.getResponseText().isEmpty())
                .count();

        long pending = total - resolved;

        tq.setText(String.valueOf(total));
        res.setText(String.valueOf(resolved));
        pen.setText(String.valueOf(pending));

        double progress = total == 0 ? 0 : (double) resolved / total;

        rBar.setProgress(progress);
        rIndi.setProgress(progress);
    }

    // ================= CLEAR FORM =================
    private void clearForm() {
        customerIdField.getSelectionModel().clearSelection();
        vehicleIdField.getSelectionModel().clearSelection();
        queryTextArea.clear();
        queryDateField.setValue(null);
    }

    // ================= ALERT =================
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }
    private void loadQueriesToVBox(ObservableList<CustomerQuery> list) {

        querl.getChildren().clear(); // IMPORTANT: reset UI

        for (CustomerQuery q : list) {

            VBox card = new VBox();
            card.setSpacing(5);
            card.setStyle("""
                -fx-background-color: white;
                -fx-padding: 12;
                -fx-background-radius: 10;
                -fx-border-color: #e0e0e0;
                -fx-border-radius: 10;
                """);

            Label id = new Label("Query ID: " + q.getQueryId());
            id.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Label vehicle = new Label("Vehicle ID: " + q.getVehicleId());
            vehicle.setStyle("-fx-text-fill: #7f8c8d;");

            Label text = new Label(q.getQueryText());
            text.setWrapText(true);
            text.setStyle("-fx-font-size: 13px;");

            Label date = new Label(q.getQueryDate());
            date.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 11px;");

            card.getChildren().addAll(id, vehicle, text, date);

            querl.getChildren().add(card);
        }
    }
}