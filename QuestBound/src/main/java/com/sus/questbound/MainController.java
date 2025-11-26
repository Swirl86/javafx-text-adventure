package com.sus.questbound;

import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextArea outputArea;

    @FXML
    private TextField inputField;

    @FXML
    public void initialize() {
        // Test setup
        Player player = new Player("Hero");
        Room startingRoom = new Room("Entrance Hall", "A grand hall with high ceilings.");
        startingRoom.addItem("Key");

        outputArea.appendText("Welcome, " + player.getName() + "!\n");
        outputArea.appendText("You are in: " + startingRoom.getName() + "\n");
        outputArea.appendText(startingRoom.getDescription() + "\n");
        outputArea.appendText("You see: " + String.join(", ", startingRoom.getItems()) + "\n");
    }

    @FXML
    public void handleInput() {
        String command = inputField.getText().trim();
        if (!command.isEmpty()) {
            outputArea.appendText("> " + command + "\n");
            inputField.clear();
            // TODO: implement command handling
        }
    }
}
