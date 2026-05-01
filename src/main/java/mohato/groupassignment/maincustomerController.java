package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;

public class maincustomerController {

    @FXML
    private MenuItem about;

    @FXML
    private Menu account;

    @FXML
    private MenuItem close;

    @FXML
    private Menu file;

    @FXML
    private Menu help;

    @FXML
    private MenuItem logout;

    @FXML
    private MenuBar menubar;

    @FXML
    private Pagination pagination;


    @FXML
    void handleabout(ActionEvent event) {
        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);

        alert.setTitle("About");
        alert.setHeaderText("Insurance Management System");
        alert.setContentText("Version 1.0\nDeveloped using JavaFX and PostgreSQL");

        alert.showAndWait();
    }

    @FXML
    void handleclose(ActionEvent event) {
        System.exit(0);
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
    @FXML
    private void initialize() {
        pagination.setPageCount(3);
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory(this::loadPage);
    }

    private javafx.scene.Node loadPage(int pageIndex) {
        try {
            switch (pageIndex) {
                case 0:
                    return loadFXML("customerdashboard.fxml");

                case 1:
                    return loadFXML("customer.fxml");

                case 2:
                    return loadFXML("customerquery.fxml");

                default:
                    return new javafx.scene.control.Label("Page not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new javafx.scene.control.Label("Error loading page");
        }
    }
    private javafx.scene.Node loadFXML(String fileName) throws Exception {
        javafx.fxml.FXMLLoader loader =
                new javafx.fxml.FXMLLoader(getClass().getResource(fileName));

        return loader.load();
    }

}
