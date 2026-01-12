package com.sus.questbound;

import com.sus.questbound.game.*;
import com.sus.questbound.game.command.*;
import com.sus.questbound.model.*;
import com.sus.questbound.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private TextFlow outputFlow;
    @FXML
    private TextField inputField;
    @FXML
    private ScrollPane outputScroll;

    private Game game;

    @FXML
    public void initialize() {
        game = new Game(new Player("Hero"));
        game.getPlayer().addItem(Item.MAP);
        game.getPlayer().addItem(Item.TORCH);

        printlnGM("Welcome, " + game.getPlayer().getName() + "!");
        enterRoom(game.getCurrentRoom());
        printlnSystem("You hear distant sounds echoing around you.\nMaybe look around to explore your surroundings.");
    }

    // ---------- printing helpers ----------
    private void appendStyled(String message, String styleClass) {
        Text t = new Text(message + "\n");
        t.getStyleClass().addAll("text", styleClass);
        Platform.runLater(() -> {
            outputFlow.getChildren().add(t);
            outputScroll.layout();
            outputScroll.setVvalue(1.0);
        });
    }

    private void printlnGM(String msg) {
        appendStyled("GM " + msg, "msg-gm");
    }

    private void printlnPlayer(String rawInput) {
        appendStyled("> " + PlayerMsgHelper.randomPlayerMsg(rawInput), "msg-player");
    }

    private void printlnSystem(String msg) {
        appendStyled("[" + msg + "]", "msg-system");
    }

    // ---------- command execution ----------
    private void executeCommand(String raw) {
        if (raw == null || raw.isEmpty()) return;

        printlnPlayer(raw); // echo player command in green
        var cmd = CommandParser.parse(raw);
        String action = cmd.action();
        String arg = cmd.argument();

        // Handle directions typed directly without 'go'
        if (PlayerMsgHelper.isDirection(raw)) {
            handleGo(raw);
            return;
        }

        switch (action) {
            case "look" -> handleLook();
            case "inventory" -> handleInventory();
            case "go" -> handleGo(arg);
            case "hint" -> showExitsHint();
            case "pickup" -> handlePickup(arg);
            case "drop" -> handleDrop(arg);
            default -> handleUnknownCommand();
        }
    }

    // ---------- input handling ----------
    @FXML
    private void handleInput() {
        String raw = inputField.getText().trim();
        inputField.clear();
        executeCommand(raw);
    }

    // ---------- Called when entering a new room ----------
    private void enterRoom(Room currentRoom) {
        printlnSystem("You are in: " + currentRoom.getName());
        printlnSystem(currentRoom.getDescription());
    }

    // ---------- game actions ----------
    private void handleLook() {
        Room currentRoom = game.getCurrentRoom();

        List<Item> items = currentRoom.getItems();
        if (items.isEmpty()) {
            printlnSystem("You see nothing of interest.");
        } else if (items.size() == 1) {
            Item item = items.get(0);
            printlnSystem("You see one item: " + item.getName() + " — " + item.getDescription());
        } else {
            String itemList = items.stream().map(Item::getName).collect(Collectors.joining(", "));
            printlnSystem("You see some items: " + itemList);
        }
    }

    private void handleInventory() {
        List<Item> inv = game.getPlayer().getInventory();
        if (inv.isEmpty()) {
            printlnSystem("Your inventory is empty.");
        } else if (inv.size() == 1) {
            Item it = inv.get(0);
            printlnSystem("You have one item: " + it.getName() + " — " + it.getDescription());
        } else {
            String names = inv.stream().map(Item::getName).collect(Collectors.joining(", "));
            printlnSystem("You have some items: " + names);
        }
    }

    private void handleGo(String direction) {
        String fullDirection = CommandAliasHelper.normalizeDirection(direction);
        MoveResult moveResult = game.move(fullDirection);

        if (moveResult.success()) {
            printlnSystem("You move " + fullDirection + "...");
            enterRoom(game.getCurrentRoom());
            return;
        }

        var availableExits = moveResult.availableExits();

        if (availableExits.isEmpty()) {
            printlnGM(GMHelper.deadEndMessage(fullDirection));
        } else {
            handleDeadEnd();
        }
    }

    private void handleUnknownCommand() {
        printlnGM(GMHelper.randomUnknownCommandHint());
    }

    private void handleDeadEnd() {
        String exits = String.join(", ", game.getCurrentRoom().getAvailableExits());
        printlnGM(GMHelper.randomDeadEndHint(exits));
    }

    private void handlePickup(String rawInput) {
        if (rawInput == null || rawInput.isEmpty()) {
            printlnSystem("Which item do you want to pick up?");
            return;
        }

        Item item = game.getCurrentRoom().getItemByName(rawInput);
        if (item == null || !game.pickupItem(rawInput)) {
            printlnGM("There is no '" + rawInput + "' here to pick up.");
            return;
        }

        printlnGM(GMHelper.randomPickupHint(item));
    }

    private void handleDrop(String rawInput) {
        if (rawInput == null || rawInput.isEmpty()) {
            printlnSystem("Which item do you want to drop?");
            return;
        }

        Item item = game.getPlayer().getItemByName(rawInput);
        if (item == null || !game.dropItem(rawInput)) {
            printlnGM("You don't have '" + rawInput + "' in your inventory.");
            return;
        }

        printlnGM(GMHelper.randomDropHint(item));
    }

    // ---------- Hint for available exits ----------
    private void showExitsHint() {
        printlnGM(GMHelper.randomHintAttempt());
        Room currentRoom = game.getCurrentRoom();
        var exits = currentRoom.getAvailableExits();

        if (exits.isEmpty()) {
            printlnSystem("There are no visible exits here.");
        } else if (exits.size() == 1) {
            printlnSystem("There is one visible exit: " + exits.iterator().next());
        } else {
            String exitList = String.join(", ", exits);
            printlnSystem("There are several visible exits: " + exitList);
        }
    }

    // ---------- Button handlers ----------
    @FXML private void onNorth() { executeCommand("north"); }
    @FXML private void onSouth() { executeCommand("south"); }
    @FXML private void onEast()  { executeCommand("east");  }
    @FXML private void onWest()  { executeCommand("west");  }
    @FXML private void onLook()  { executeCommand("look"); }
    @FXML private void onInventory() { executeCommand("inventory"); }
    @FXML private void onHint() { executeCommand("hint"); }
    @FXML
    private void onPickup() {
        List<Item> itemsInRoom = game.getCurrentRoom().getItems();

        if (itemsInRoom.isEmpty()) {
            printlnSystem("You reach out, but grab nothing. The air feels heavier for it.");
            return;
        }

        List<String> itemNames = itemsInRoom.stream()
                .map(Item::getName)
                .collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(itemNames.get(0), itemNames);
        dialog.setTitle("Pick up item");
        dialog.setHeaderText("Choose an item to pick up:");
        dialog.setContentText("Available items:");

        DialogPane pane = dialog.getDialogPane();
        pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/sus/questbound/style.css")).toExternalForm());

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::handlePickup);
    }

    @FXML
    private void onDrop() {
        List<Item> inventory = game.getPlayer().getInventory();

        if (inventory.isEmpty()) {
            printlnSystem("You look into your empty pockets. Nothing to drop here!");
            return;
        }

        List<String> itemNames = inventory.stream()
                .map(Item::getName)
                .collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(itemNames.get(0), itemNames);
        dialog.setTitle("Drop item");
        dialog.setHeaderText("Choose an item to drop:");
        dialog.setContentText("Your inventory:");

        DialogPane pane = dialog.getDialogPane();
        pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/sus/questbound/style.css")).toExternalForm());

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::handleDrop);
    }


    //TODO implement
    @FXML private void onQuest(ActionEvent actionEvent) { }
    @FXML private void onStatus(ActionEvent actionEvent) { }
    @FXML private void onUse(ActionEvent actionEvent) { }
    @FXML private void onMap(ActionEvent actionEvent) { }

}
