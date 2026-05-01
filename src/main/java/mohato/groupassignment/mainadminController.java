package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;

public class mainadminController {

    @FXML
    private MenuBar menubar;

    @FXML
    private Pagination pagination;

    @FXML
    private BorderPane rootPane;

    // =========================
    // DASHBOARD
    // =========================
    @FXML
    void handledashboard(ActionEvent event) {
        loadPage("admindasboard.fxml");
    }


    @FXML
    void handleuser(ActionEvent event) {
        loadPage("users.fxml");
    }

    // =========================
    // VEHICLES
    // =========================
    @FXML
    void handlevehicle(ActionEvent event) {
        loadPage("vehicle.fxml");
    }

    // =========================
    // REPORTS
    // =========================
    @FXML
    void handlereports(ActionEvent event) {
        loadPage("reports.fxml");
    }

    // =========================
    // SETTINGS
    // =========================
    @FXML
    void handlesettings(ActionEvent event) {
        loadPage("settings.fxml");
    }

    // =========================
    // CORE LOADER METHOD
    // =========================
    private void loadPage(String fxmlFile) {
        try {
            Parent page = FXMLLoader.load(
                    getClass().getResource(fxmlFile)
            );

            rootPane.setCenter(page);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}