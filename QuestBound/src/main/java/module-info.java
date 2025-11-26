module com.sus.questbound {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sus.questbound to javafx.fxml;
    exports com.sus.questbound;
}