package mohato.groupassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import java.sql.Connection;
import java.sql.ResultSet;

public class policedashboardController {

    @FXML
    private VBox activityBox;



    @FXML
    private Label attendedReports;

    @FXML
    private ProgressBar pb;

    @FXML
    private ProgressIndicator pi;

    @FXML
    private ScrollPane sp;

    @FXML
    private Label totalReports;

    @FXML
    private Label totalVehicles;





    @FXML
    private Label unattendedReports;



    @FXML
    public void initialize() {
        loadDashboard();
    }

    private void loadDashboard() {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        Connection conn = db.getConn();

        try {

            // TOTAL REPORTS
            ResultSet rs1 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM police_report"
            );
            rs1.next();
            int total = rs1.getInt(1);

            // ATTENDED REPORTS
            ResultSet rs2 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM police_report WHERE officer_name IS NOT NULL"
            );
            rs2.next();
            int attended = rs2.getInt(1);

            int unattended = total - attended;

            // VEHICLES
            ResultSet rs3 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM vehicle"
            );
            rs3.next();
            int vehicles = rs3.getInt(1);

            // UPDATE UI
            totalReports.setText(String.valueOf(total));
            attendedReports.setText(String.valueOf(attended));
            unattendedReports.setText(String.valueOf(unattended));
            totalVehicles.setText(String.valueOf(vehicles));

            // PROGRESS
            double progress = (total == 0) ? 0 : (double) attended / total;

            pb.setProgress(progress);
            pi.setProgress(progress);

            // Activity log
            addActivity("Dashboard loaded successfully");
            addActivity("Reports: " + total);
            addActivity("Vehicles: " + vehicles);

        } catch (Exception e) {
            e.printStackTrace();
            addActivity("Error loading dashboard");
        }
    }

    private void addActivity(String msg) {

        Label log = new Label("• " + msg);
        log.setStyle("-fx-text-fill:#2c3e50; -fx-font-size:12px;");

        activityBox.getChildren().add(log);

        sp.setVvalue(1.0); // auto scroll down
    }

    public void refreshDashboard() {
        loadDashboard();
    }

}
