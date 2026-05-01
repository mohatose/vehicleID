package mohato.groupassignment;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ActivityLogger {

    private static VBox logContainer;

    public static void setContainer(VBox baseBox) {
        logContainer = baseBox;
    }

    public static void log(String message) {

        if (logContainer == null) return;

        Label entry = new Label("• " + message);
        entry.setStyle("-fx-text-fill: #2c3e50; -fx-font-size: 12px;");

        logContainer.getChildren().add(entry);
    }
}