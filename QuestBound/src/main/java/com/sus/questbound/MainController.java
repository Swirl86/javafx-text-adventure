package com.sus.questbound;

import com.sus.questbound.game.*;
import com.sus.questbound.model.*;
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

    private Game game;
    private Player player;

    @FXML
    public void initialize() {
        // Test setup
        player = new Player("Hero");
        player.addItem(Item.MAP);
        player.addItem(Item.TORCH);

        Player player = new Player("Hero");
        game = new Game(player);

        println("GM says: Welcome, " + player.getName() + "!");
        println("You hear distant sounds echoing around you. Type 'look' to explore your surroundings.");
        println("Curious about what you carry? Type 'inventory' to check your belongings.");
    }

    @FXML
    private void handleInput() {
        String raw = inputField.getText().trim();
        inputField.clear();

        if (raw.isEmpty()) {
            return;
        }

        Command cmd = CommandParser.parse(raw);

        switch (cmd.action()) {
            case "look" -> handleLook();
            case "inventory" -> handleInventory();
            case "go" -> handleGo(cmd.argument());
            default -> handleUnknownCommand();
        }
    }

    private void handleGo(String direction) {
        String fullDirection = CommandAliasHelper.normalizeDirection(direction);
        MoveResult result = game.move(fullDirection);

        if (result.success()) {
            println("You move " + fullDirection + ". . .");
            handleLook();
        } else {
            List<String> exits = result.availableExits();
            if (exits.isEmpty()) {
                println("GM says: You try to go " + fullDirection + " but hit a dead end.\nYou can only look around or go back the way you came.");
            } else {
                handleDeadEnd(exits);
            }
        }
    }

    private void handleLook() {
        Room currentRoom = game.getCurrentRoom();
        outputArea.appendText("You are in: " + currentRoom.getName() + "\n");
        outputArea.appendText(currentRoom.getDescription() + "\n");

        List<Item> items = currentRoom.getItems();
        if (items.isEmpty()) {
            outputArea.appendText("You see nothing of interest.\n");
        } else if (items.size() == 1) {
            Item item = items.get(0);
            outputArea.appendText("You see one item: " + item.getName() + " - " + item.getDescription() + "\n");
        } else {
            String itemList = items.stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            outputArea.appendText("You see some items: " + itemList + "\n");
        }
    }

    private void handleInventory() {
        List<Item> inventory = player.getInventory();

        if (inventory.isEmpty()) {
            outputArea.appendText("Your inventory is empty.\n");
        } else if (inventory.size() == 1) {
            Item item = inventory.get(0);
            outputArea.appendText("You have one item: " + item.getName() + " - " + item.getDescription() + "\n");
        } else {
            String itemList = inventory.stream()
                    .map(Item::getName)
                    .collect(Collectors.joining(", "));
            outputArea.appendText("You have some items: " + itemList + "\n");
        }
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
        println(response + "\n");
    }

    private void handleDeadEnd(List<String> exits) {
        List<String> gmHints = List.of(
                "GM whispers: Oops, no door that way! Available exits: " + String.join(", ", exits),
                "GM chuckles: That direction seems blocked. You could try: " + String.join(", ", exits),
                "GM remarks: Hmm, nothing but walls that way. Your options: " + String.join(", ", exits)
        );
        Random rand = new Random();
        println(gmHints.get(rand.nextInt(gmHints.size())));
    }

    private void println(String text) {
        outputArea.appendText(text + "\n");
    }
}
