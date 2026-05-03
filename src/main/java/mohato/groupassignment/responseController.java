package mohato.groupassignment;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class responseController {

    @FXML private TableView<CustomerQuery> queryTable;

    @FXML private TableColumn<CustomerQuery, Integer> colQueryId;
    @FXML private TableColumn<CustomerQuery, Integer> colCustomerId1;
    @FXML private TableColumn<CustomerQuery, Integer> colVehicleId;
    @FXML private TableColumn<CustomerQuery, String> colDate;
    @FXML private TableColumn<CustomerQuery, String> colQueryText;
    @FXML private TableColumn<CustomerQuery, String> colResponse;

    // NEW: Status column
    @FXML private TableColumn<CustomerQuery, String> colStatus;

    @FXML private TextArea queryrespond;
    @FXML private Button respondBtn;

    @FXML private Label tq, pen, res;
    @FXML private ProgressBar rBar;
    @FXML private ProgressIndicator rIndi;

    // NEW: search field (must exist in FXML)
    @FXML private TextField searchField;

    private ObservableList<CustomerQuery> masterList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        loadQueries();
        setupSearch();
        setupRowClick();
    }

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

        // ✅ Status column logic
        colStatus.setCellValueFactory(d -> {
            String status = (d.getValue().getResponseText() == null ||
                    d.getValue().getResponseText().isEmpty())
                    ? "Pending" : "Resolved";

            return new javafx.beans.property.SimpleStringProperty(status);
        });
    }

    // 🔎 SEARCH FEATURE
    private void setupSearch() {

        FilteredList<CustomerQuery> filtered = new FilteredList<>(masterList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {

            filtered.setPredicate(query -> {

                if (newVal == null || newVal.isEmpty()) return true;

                String keyword = newVal.toLowerCase();

                return String.valueOf(query.getQueryId()).contains(keyword)
                        || String.valueOf(query.getCustomerId()).contains(keyword)
                        || query.getQueryText().toLowerCase().contains(keyword)
                        || (query.getResponseText() != null &&
                        query.getResponseText().toLowerCase().contains(keyword));
            });
        });

        queryTable.setItems(filtered);
    }

    // 🎯 CLICK ROW → AUTO-FILL RESPONSE
    private void setupRowClick() {
        queryTable.setOnMouseClicked(event -> {

            CustomerQuery selected = queryTable.getSelectionModel().getSelectedItem();

            if (selected != null) {
                queryrespond.setText(
                        selected.getResponseText() == null ? "" : selected.getResponseText()
                );

                animateFade(queryrespond);
            }
        });
    }

    @FXML
    void handlerespond(ActionEvent event) {

        CustomerQuery selected = queryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "No query selected!", Alert.AlertType.WARNING);
            return;
        }

        if (queryrespond.getText().isEmpty()) {
            showAlert("Warning", "Response cannot be empty!", Alert.AlertType.WARNING);
            return;
        }

        try (Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn()) {

            String sql = "UPDATE customer_query SET response_text = ? WHERE query_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, queryrespond.getText());
            ps.setInt(2, selected.getQueryId());

            ps.executeUpdate();

            showAlert("Success", "Response submitted successfully!", Alert.AlertType.INFORMATION);

            ActivityLogger.log("Responded to query ID: " + selected.getQueryId());

            loadQueries();
            queryrespond.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Database error occurred!", Alert.AlertType.ERROR);
        }
    }

    private void loadQueries() {

        masterList.clear();

        try (Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn()) {

            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT * FROM customer_query ORDER BY query_id DESC"
            );

            while (rs.next()) {

                masterList.add(new CustomerQuery(
                        rs.getInt("query_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getTimestamp("query_date").toString(),
                        rs.getString("query_text"),
                        rs.getString("response_text")
                ));
            }

            updateStats(masterList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load queries!", Alert.AlertType.ERROR);
        }
    }

    private void updateStats(ObservableList<CustomerQuery> list) {

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

    // 🎨 SIMPLE ANIMATION
    private void animateFade(Control node) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), node);
        ft.setFromValue(0.3);
        ft.setToValue(1);
        ft.play();
    }

    // ⚠️ POPUP ALERT SYSTEM
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}