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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.ObservableList;

public class servicerecordController {

    @FXML
    private TextField costField;

    @FXML
    private TableColumn<ServiceRecord, Integer> serveidCol;

    @FXML
    private TableColumn<ServiceRecord, Integer> vehiidCol;

    @FXML
    private TableColumn<ServiceRecord, String> servedateCol;

    @FXML
    private TableColumn<ServiceRecord, String> servetypeCol;

    @FXML
    private TableColumn<ServiceRecord, String> descripCol;

    @FXML
    private TableColumn<ServiceRecord, Double> costsCol;

    @FXML
    private TextArea destypeField;

    @FXML
    private ColumnConstraints gridpaneField;

    @FXML
    private VBox pCard;

    @FXML
    private Label pse;

    @FXML
    private Button savesBtn;

    @FXML
    private TextField sdateField;

    @FXML
    private ProgressBar serveBar;

    @FXML
    private ProgressIndicator serveIndi;

    @FXML
    private ScrollPane serveScroll;

    @FXML
    private TableView<ServiceRecord> serveTable;





    @FXML
    private TextField serviceidField;

    @FXML
    private TextField stype;

    @FXML
    private VBox tCard;

    @FXML
    private Label totas;

    @FXML
    private VBox upCard;

    @FXML
    private Label ups;

    @FXML
    private VBox vact;



    @FXML
    private TextField vidField;

    @FXML
    void handleseveservice(ActionEvent event) {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql =
                    "INSERT INTO service_record (vehicle_id, service_date, service_type, description, cost) " +
                            "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(vidField.getText()));
            ps.setDate(2, java.sql.Date.valueOf(sdateField.getText()));
            ps.setString(3, stype.getText());
            ps.setString(4, destypeField.getText());
            ps.setDouble(5, Double.parseDouble(costField.getText()));

            ps.executeUpdate();

            addActivity("Service record saved");

            clearFields();
            loadServices();

        } catch (Exception e) {
            e.printStackTrace();
            addActivity("Error saving service record");
        }
    }
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

        loadServices();
    }

    private void loadServices() {

        ObservableList<ServiceRecord> list =
                javafx.collections.FXCollections.observableArrayList();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT * FROM service_record"
            );

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
            e.printStackTrace();
        }
    }
    private void clearFields() {

        serviceidField.clear();
        vidField.clear();
        sdateField.clear();
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



}
