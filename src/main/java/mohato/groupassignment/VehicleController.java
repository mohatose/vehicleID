package mohato.groupassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VehicleController {

    @FXML
    private VBox RegiCard;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> makeCol;

    @FXML
    private TextField makeField;

    @FXML
    private TableColumn<?, ?> modelCol;

    @FXML
    private TextField modelField;

    @FXML
    private TableColumn<?, ?> ownerCol;

    @FXML
    private TextField ownerIdField;

    @FXML
    private TableColumn<?, ?> regNoCol;

    @FXML
    private TextField regNumberField;

    @FXML
    private VBox tCard;

    @FXML
    private VBox unregiCard;

    @FXML
    private ScrollPane vScroll;

    @FXML
    private HBox vclBar;

    @FXML
    private VBox vclCard;

    @FXML
    private TextField vehicleIdField;

    @FXML
    private TableView<?> vehicleTable;

    @FXML
    private ProgressIndicator vlcIndi;

    @FXML
    private TableColumn<?, ?> yearCol;

    @FXML
    private TextField yearField;

    @FXML
    void handleAddVehicle(ActionEvent event) {

    }

}
