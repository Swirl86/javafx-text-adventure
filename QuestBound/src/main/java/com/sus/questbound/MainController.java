package com.sus.questbound;

import com.sus.questbound.model.Item;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private TextArea outputArea;

    @FXML
    private TextField inputField;

    private Player player;
    private Room currentRoom;

    @FXML
    public void initialize() {
        // Test setup
        player = new Player("Hero");
        player.addItem(Item.MAP);
        player.addItem(Item.TORCH);

        currentRoom = new Room("Entrance Hall", "A grand hall with high ceilings.\nThe walls are adorned with faded tapestries and an old chandelier hangs from above.");
        currentRoom.addItem(Item.KEY);
        currentRoom.addItem(Item.LANTERN);
        currentRoom.addItem(Item.OLD_BOOK);

        println("GM says: Welcome, " + player.getName() + "!");
        println("You hear distant sounds echoing around you. Type 'look' to explore your surroundings.");
        println("Curious about what you carry? Type 'inventory' to check your belongings.");
    }

    @FXML
    private void handleInput() {
        String command = inputField.getText().trim().toLowerCase();
        inputField.clear();

        if (command.isEmpty()) {
            return;
        }

        switch (command) {
            case "look" -> handleLook();
            case "inventory" -> handleInventory();
            default -> handleUnknownCommand();
        }
    }

    private void handleLook() {
        outputArea.appendText("You are in: " + currentRoom.getName() + "\n");
        outputArea.appendText(currentRoom.getDescription() + "\n");
        if (currentRoom.getItems().isEmpty()) {
            outputArea.appendText("You see nothing of interest.\n");
        } else {
            String items = currentRoom.getItems().stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            outputArea.appendText("You see: " + items + "\n");
        }
    }

    private void handleInventory() {
        if (player.getInventory().isEmpty()) {
            outputArea.appendText("Your inventory is empty.\n");
        } else {
            String items = player.getInventory().stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            outputArea.appendText("Inventory: " + items + "\n");
        }
    }

    private void println(String text) {
        outputArea.appendText(text + "\n");
    }

    private void handleUnknownCommand() {
        List<String> gmResponses = List.of(
                "GM whispers: Hmm, that was a bit unusual. Try 'look' to see around\nor 'inventory' to check your items.",
                "GM chuckles: That command seems odd! Maybe you meant to look around\nor check your inventory?",
                "GM says: Oops, I don't understand that. You could type 'look' to explore\nor 'inventory' to see what you have.",
                "GM smirks: Interesting choice... but try 'look' or 'inventory' instead.",
                "GM remarks: That doesn't quite work. Perhaps try looking around ('look')\nor checking your inventory ('inventory')."
        );

        Random rand = new Random();
        String response = gmResponses.get(rand.nextInt(gmResponses.size()));
        outputArea.appendText(response + "\n");
    }
}
