package mohato.groupassignment;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class admindasboardController implements Initializable {

    @FXML private Label tu;
    @FXML private Label rv;
    @FXML private Label tr;
    @FXML private Label tq;

    @FXML private ProgressBar sl;
    @FXML private ProgressIndicator dp;

    @FXML private VBox vb;
    @FXML private VBox chartContainer;

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
        loadDashboardData();
        loadActivities();
        loadCharts();
    }

    // ================= DASHBOARD STATS =================
    private void loadDashboardData() {

        try (Connection conn = connect()) {

            // USERS
            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) tu.setText(String.valueOf(rs1.getInt(1)));

            // VEHICLES
            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM vehicle");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) rv.setText(String.valueOf(rs2.getInt(1)));

            // REPORTS (FIXED TABLE NAME)
            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM police_report");
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) tr.setText(String.valueOf(rs3.getInt(1)));

            // QUERIES (FIXED TABLE NAME)
            PreparedStatement ps4 = conn.prepareStatement("SELECT COUNT(*) FROM customer_query");
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) tq.setText(String.valueOf(rs4.getInt(1)));

            // ================= PROGRESS FIX =================
            int users = Integer.parseInt(tu.getText());
            int vehicles = Integer.parseInt(rv.getText());

            double load = Math.min(1.0, (users + vehicles) / 200.0);

            sl.setProgress(load);
            dp.setProgress(load);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= ACTIVITIES =================
    private void loadActivities() {

        vb.getChildren().clear();

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

                lbl.setStyle("-fx-padding: 5; -fx-text-fill: #333;");
                vb.getChildren().add(lbl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CHARTS =================
    private void loadCharts() {

        chartContainer.getChildren().clear();

        try (Connection conn = connect()) {

            // PIE CHART (Reports vs Queries)
            PieChart pieChart = new PieChart();

            PreparedStatement ps1 = conn.prepareStatement("SELECT COUNT(*) FROM police_report");
            ResultSet rs1 = ps1.executeQuery();
            rs1.next();
            int reports = rs1.getInt(1);

            PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM customer_query");
            ResultSet rs2 = ps2.executeQuery();
            rs2.next();
            int queries = rs2.getInt(1);

            pieChart.getData().add(new PieChart.Data("Reports", reports));
            pieChart.getData().add(new PieChart.Data("Queries", queries));

            pieChart.setTitle("Reports vs Queries");

            // BAR CHART (System Overview)
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("System Overview");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Counts");

            PreparedStatement ps3 = conn.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs3 = ps3.executeQuery();
            rs3.next();

            PreparedStatement ps4 = conn.prepareStatement("SELECT COUNT(*) FROM vehicle");
            ResultSet rs4 = ps4.executeQuery();
            rs4.next();

            series.getData().add(new XYChart.Data<>("Users", rs3.getInt(1)));
            series.getData().add(new XYChart.Data<>("Vehicles", rs4.getInt(1)));
            series.getData().add(new XYChart.Data<>("Reports", reports));

            barChart.getData().add(series);

            chartContainer.getChildren().addAll(pieChart, barChart);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= REFRESH BUTTON =================
    @FXML
    private void handlerefresh() {
        loadDashboardData();
        loadActivities();
        loadCharts();
    }

}