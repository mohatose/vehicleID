package mohato.groupassignment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class customerdashboardController implements Initializable {

    @FXML private Label basotho;      // vehicles
    @FXML private Label lesotho;      // insurance policies
    @FXML private Label lithabeng;    // violations
    @FXML private Label majahapere;   // customers

    @FXML private ProgressBar pula;
    @FXML private ProgressIndicator nala;

    @FXML private VBox khotso;

    // ================= DB CONNECTION =================
    private Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/vehicle_system",
                    "postgres",
                    "ntj@nalanga#2$8"
            );
            System.out.println("Connected to PostgreSQL!");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ================= INIT =================
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadStats();
        loadActivities();
        loadProgress();
    }

    // ================= LOAD KPI DATA =================
    private void loadStats() {

        try (Connection conn = connect()) {

            // VEHICLES
            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM vehicle");
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) basotho.setText(String.valueOf(rs1.getInt(1)));

            // ACTIVE POLICIES (insurance table)
            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM insurance");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) lesotho.setText(String.valueOf(rs2.getInt(1)));

            // VIOLATIONS
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM violation");
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) lithabeng.setText(String.valueOf(rs3.getInt(1)));

            // CUSTOMERS
            PreparedStatement ps4 = conn.prepareStatement("SELECT COUNT(*) FROM customer");
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) majahapere.setText(String.valueOf(rs4.getInt(1)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SYSTEM PERFORMANCE =================
    private void loadProgress() {

        try {

            int vehicles = Integer.parseInt(basotho.getText());
            int policies = Integer.parseInt(lesotho.getText());

            double performance = Math.min(1.0, (vehicles + policies) / 200.0);

            pula.setProgress(performance);
            nala.setProgress(performance);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ACTIVITY FEED =================
    private void loadActivities() {

        khotso.getChildren().clear();

        try (Connection conn = connect()) {

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT message, created_at FROM activity_log ORDER BY created_at DESC LIMIT 10"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Label lbl = new Label(
                        "• " + rs.getString("message") +
                                " (" + rs.getTimestamp("created_at") + ")"
                );

                lbl.setStyle("-fx-padding:5; -fx-text-fill:#333;");
                khotso.getChildren().add(lbl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void insertActivity(String message) {

        String sql = "INSERT INTO activity_log(message) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/vehicle_system",
                "postgres",
                "ntj@nalanga#2$8"
        );
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, message);
            ps.executeUpdate();

            System.out.println("Activity inserted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}