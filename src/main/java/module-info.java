module mohato.groupassignment {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;

    opens mohato.groupassignment to javafx.fxml;
    exports mohato.groupassignment;
}