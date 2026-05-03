package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;

public class mainadminController {

    @FXML
    private MenuBar menubar;

    @FXML
    private Pagination pagination;

    @FXML
    private BorderPane rootPane;

    @FXML
    private MenuItem logout;

    @FXML
    void handledashboard(ActionEvent event) {
        loadPage("admindasboard.fxml");
    }


    @FXML
    void handleuser(ActionEvent event) {
        loadPage("users.fxml");
    }


    @FXML
    void handlevehicle(ActionEvent event) {
        loadPage("workshop.fxml");
    }


    @FXML
    void handlereports(ActionEvent event) {
        loadPage("police.fxml");
    }

    @FXML
    void handlequery(ActionEvent event) {
        loadPage("responses.fxml");
    }

    @FXML
    void handleinsurance(ActionEvent event) {
        loadPage("insurance.fxml");
    }

    @FXML
    void handlesettings(ActionEvent event) {
        loadPage("settings.fxml");
    }


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
    @FXML
    void handlelogout(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(getClass().getResource("login.fxml"));

            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage =
                    (javafx.stage.Stage) pagination.getScene().getWindow();

            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}