package mohato.groupassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.ResultSet;

public class workshopdashboardController {

    @FXML
    private VBox base;

    @FXML
    private Label cs;

    @FXML
    private ProgressIndicator indic;

    @FXML
    private ProgressBar prom;

    @FXML
    private Label psss;

    @FXML
    private Label st;

    @FXML
    private Label tv;

    // =========================
    // INITIALIZE (FIXED)
    // =========================
    @FXML
    public void initialize() {

        ActivityLogger.setContainer(base);

        loadDashboardStats();

        ActivityLogger.log("Dashboard initialized");
    }

    // =========================
    // LOAD DASHBOARD STATS
    // =========================
    private void loadDashboardStats() {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            // 1. TOTAL VEHICLES
            ResultSet rs1 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM vehicle"
            );
            rs1.next();
            int totalVehicles = rs1.getInt(1);

            // 2. TOTAL SERVICES
            ResultSet rs2 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM service_record"
            );
            rs2.next();
            int totalServices = rs2.getInt(1);

            // 3. COMPLETED SERVICES (cost > 0)
            ResultSet rs3 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM service_record WHERE cost > 0"
            );
            rs3.next();
            int completed = rs3.getInt(1);

            // 4. PENDING
            int pending = totalServices - completed;

            // =========================
            // UPDATE UI
            // =========================
            tv.setText(String.valueOf(totalVehicles));
            st.setText(String.valueOf(totalServices));
            cs.setText(String.valueOf(completed));
            psss.setText(String.valueOf(pending));

            double progress = totalServices == 0 ? 0 : (double) completed / totalServices;

            prom.setProgress(progress);
            indic.setProgress(progress);

        } catch (Exception e) {
            e.printStackTrace();
            ActivityLogger.log("Dashboard error loading stats");
        }
    }

    // =========================
    // REFRESH BUTTON
    // =========================
    @FXML
    private void refreshDashboard() {
        loadDashboardStats();
        ActivityLogger.log("Dashboard refreshed");
    }
}