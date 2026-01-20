module com.sus.questbound {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    opens com.sus.questbound to javafx.fxml;

    opens com.sus.questbound.view to javafx.fxml;
    opens com.sus.questbound.game.library to com.fasterxml.jackson.databind;
    opens com.sus.questbound.model to com.fasterxml.jackson.databind;

    exports com.sus.questbound;
}