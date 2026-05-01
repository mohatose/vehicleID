package mohato.groupassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class insurancedashboardController {

    @FXML
    private Label ap;

    @FXML
    private VBox ccard1;

    @FXML
    private VBox ccard2;

    @FXML
    private VBox ccard3;

    @FXML
    private Label claip;

    @FXML
    private Label ep;

    @FXML
    private Label n1;

    @FXML
    private Label n2;

    @FXML
    private Label n3;

    @FXML
    private Label n4;

    @FXML
    private ProgressBar pro1;

    @FXML
    private ProgressIndicator pro2;

    @FXML
    private ScrollPane scrollinsu;

    @FXML
    private VBox space;

    @FXML
    private Label veht;

    @FXML
    public void initialize() {
        loadDashboard();
        addActivity("Dashboard loaded");
    }

    private void loadDashboard() {

        DBConnection db = new DBConnection("postgres", "ntj@nalanga#2$8");
        java.sql.Connection conn = db.getConn();

        try {

            // TOTAL VEHICLES
            var rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM vehicle");
            rs1.next();
            int totalVehicles = rs1.getInt(1);

            // ACTIVE POLICIES (not expired)
            var rs2 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM insurance WHERE end_date >= CURRENT_DATE");
            rs2.next();
            int activePolicies = rs2.getInt(1);

            // EXPIRED POLICIES
            var rs3 = conn.createStatement().executeQuery(
                    "SELECT COUNT(*) FROM insurance WHERE end_date < CURRENT_DATE");
            rs3.next();
            int expiredPolicies = rs3.getInt(1);

            // CLAIMS (you don’t have table yet → keep 0)
            int claims = 0;

            // SET VALUES
            n1.setText(String.valueOf(totalVehicles));
            n2.setText(String.valueOf(activePolicies));
            n3.setText(String.valueOf(expiredPolicies));
            n4.setText(String.valueOf(claims));

            // PROGRESS
            double progress = totalVehicles == 0 ? 0 :
                    (double) activePolicies / totalVehicles;

            pro1.setProgress(progress);
            pro2.setProgress(progress);

        } catch (Exception e) {
            addActivity("Error loading dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addActivity(String message) {

        Label log = new Label("✔ " + message);
        log.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");

        space.getChildren().add(log);

        // auto scroll down
        scrollinsu.setVvalue(1.0);
    }

}
