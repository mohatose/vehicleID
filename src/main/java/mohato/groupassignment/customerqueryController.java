package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

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
    private TextField customerIdField;

    @FXML
    private VBox pCard;

    @FXML
    private Label pen;

    @FXML
    private ScrollPane qScroll;

    @FXML
    private VBox querl;

    @FXML
    private TextField queryDateField;

    @FXML
    private TextArea queryTextArea;

    @FXML
    private ProgressBar rBar;

    @FXML
    private VBox rCard;

    @FXML
    private ProgressIndicator rIndi;

    @FXML
    private Label res;

    @FXML
    private Button sendQueryBtn;

    @FXML
    private Label tq;

    @FXML
    private VBox ttCard;

    @FXML
    private ComboBox<Integer> vehicleIdField;

    @FXML
    void handlesendquery(ActionEvent event) {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "INSERT INTO customer_query (customer_id, vehicle_id, query_text) VALUES (?, ?, ?)";

            java.sql.PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(customerIdField.getText()));
            ps.setInt(2, vehicleIdField.getValue());
            ps.setString(3, queryTextArea.getText());

            ps.executeUpdate();

            ActivityLogger.log("New query submitted by customer " + customerIdField.getText());

            loadQueries();

            customerIdField.clear();
            queryTextArea.clear();

        } catch (Exception e) {
            e.printStackTrace();
            ActivityLogger.log("Error submitting query");
        }
    }
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

    @FXML
    public void initialize() {

        setupTable();
        loadVehiclesIntoCombo();
        loadQueries();
    }

    private void setupTable() {

        colQueryId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQueryId()).asObject());

        colCustomerId1.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCustomerId()).asObject());

        colVehicleId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getVehicleId()).asObject());

        colDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getQueryDate()));

        colQueryText.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getQueryText()));

        colResponse.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getResponseText()));
    }

    private void loadVehiclesIntoCombo() {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT vehicle_id FROM vehicle"
            );

            while (rs.next()) {
                vehicleIdField.getItems().add(rs.getInt("vehicle_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadQueries() {

        javafx.collections.ObservableList<CustomerQuery> list =
                javafx.collections.FXCollections.observableArrayList();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT * FROM customer_query"
            );

            while (rs.next()) {

                list.add(new CustomerQuery(
                        rs.getInt("query_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getTimestamp("query_date").toString(),
                        rs.getString("query_text"),
                        rs.getString("response_text")
                ));
            }

            queryTable.setItems(list);

            updateStats(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
