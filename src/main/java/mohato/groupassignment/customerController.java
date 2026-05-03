package mohato.groupassignment;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import java.sql.*;
import javafx.scene.control.*;

public class customerController{

    @FXML
    private TableView<Customer> table;

    @FXML
    private Label activ;

    @FXML
    private TextField addressf;

    @FXML
    private Button clear;

    @FXML
    private TableColumn<Customer, Integer> idcol;

    @FXML
    private TableColumn<Customer, String> ncol;

    @FXML
    private TableColumn<Customer, String> acol;

    @FXML
    private TableColumn<Customer, String> pcol;

    @FXML private TableColumn<Customer, String> ecol;

    @FXML
    private TextField emailf;



    @FXML
    private Label inactiv;

    @FXML
    private TextField namef;





    @FXML
    private TextField phonef;

    @FXML
    private ProgressBar pi;

    @FXML
    private ProgressIndicator pil;

    @FXML
    private Button save;

    @FXML
    private Label totalCustomer;

    @FXML
    private Button update;



    @FXML
    public void initialize() {
        bindColumns();
        loadCustomers();
    }

    private void bindColumns() {

        idcol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getCustomerId()
                ).asObject()
        );

        ncol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        acol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAddress()
                )
        );

        pcol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPhone()
                )
        );

        ecol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getEmail()
                )
        );
    }

    private void loadCustomers() {

        ObservableList<Customer> list =
                javafx.collections.FXCollections.observableArrayList();

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            ResultSet rs = conn.createStatement()
                    .executeQuery("SELECT * FROM customer");

            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }

            table.setItems(list);
            updateStats(list);

        } catch (Exception e) {
            showAlert("Database Error", "Failed to load customers from database.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void hadlesave(ActionEvent event) {

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "INSERT INTO customer(name, address, phone, email) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, namef.getText());
            ps.setString(2, addressf.getText());
            ps.setString(3, phonef.getText());
            ps.setString(4, emailf.getText());

            ps.executeUpdate();

            showAlert("Success", "Customer saved successfully!", Alert.AlertType.INFORMATION);

            clearFields();
            loadCustomers();

        } catch (Exception e) {
            showAlert("Database Error", "Failed to save customer.\nCheck input or connection.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleupdate(ActionEvent event) {

        Customer selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Warning", "Please select a customer first.", Alert.AlertType.WARNING);
            return;
        }

        try {
            Connection conn = new DBConnection("postgres", "ntj@nalanga#2$8").getConn();

            String sql = "UPDATE customer SET name=?, address=?, phone=?, email=? WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, namef.getText());
            ps.setString(2, addressf.getText());
            ps.setString(3, phonef.getText());
            ps.setString(4, emailf.getText());
            ps.setInt(5, selected.getCustomerId());

            ps.executeUpdate();

            showAlert("Success", "Customer updated successfully!", Alert.AlertType.INFORMATION);

            loadCustomers();

        } catch (Exception e) {
            showAlert("Database Error", "Failed to update customer.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handleclear(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        namef.clear();
        addressf.clear();
        phonef.clear();
        emailf.clear();
    }

    private void updateStats(ObservableList<Customer> list) {

        int total = list.size();

        totalCustomer.setText(String.valueOf(total));

        double progress = total == 0 ? 0 : 1.0;

        pi.setProgress(progress);
        pil.setProgress(progress);
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
