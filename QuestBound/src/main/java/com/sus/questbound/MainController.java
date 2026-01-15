package com.sus.questbound;

import com.sus.questbound.game.Game;
import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import com.sus.questbound.ui.ActionController;
import com.sus.questbound.ui.OutputController;
import com.sus.questbound.util.*;
import com.sus.questbound.game.MoveResult;
import com.sus.questbound.game.command.CommandParser;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TextFlow outputFlow;
    @FXML private TextField inputField;
    @FXML private ScrollPane outputScroll;

    private GameLogicController gameLogic;
    private ActionController actions;
    private OutputController outputController;

    // ---------- lifecycle ----------
    @FXML
    public void initialize() {
        Player player = new Player("Hero");
        player.addItem(Item.MAP);
        player.addItem(Item.TORCH);

        Game game = new Game(player);
        gameLogic = new GameLogicController(game);

        outputController = new OutputController(outputFlow, outputScroll);

        actions = new ActionController(
                gameLogic,
                this::executeCommand,
                outputController::printlnSystem
        );

        outputController.printlnSystem(SystemMsgHelper.randomWelcome(gameLogic.getPlayer()));
        enterRoom(gameLogic.getCurrentRoom());
    }

    // ---------- input ----------
    @FXML
    private void handleInput() {
        String raw = inputField.getText().trim();
        inputField.clear();
        executeCommand(raw);
    }

    // ---------- command execution ----------
    private void executeCommand(String raw) {
        if (raw == null || raw.isBlank()) return;

        outputController.printlnPlayer(raw);

        var cmd = CommandParser.parse(raw);
        String action = cmd.action();
        String arg = cmd.argument();

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

    // ---------- game gameLogic ----------
    private void enterRoom(Room room) {
        outputController.printlnSystem(SystemMsgHelper.enterRoomMessage(room));
    }

    private void handleLook() {
        List<Item> items = gameLogic.getCurrentRoom().getItems();

        if (items.isEmpty()) {
            outputController.printlnSystem(SystemMsgHelper.nothingToSee());
        } else if (items.size() == 1) {
            Item it = items.get(0);
            outputController.printlnSystem(SystemMsgHelper.singleItemInRoom(it.getName(), it.getDescription()));
        } else {
            String names = items.stream().map(Item::getName).collect(Collectors.joining(", "));
            outputController.printlnSystem(SystemMsgHelper.multipleItemsInRoom(names));
        }
    }

    private void handleInventory() {
        List<Item> inv = gameLogic.getPlayerInventory();

        if (inv.isEmpty()) {
            outputController.printlnSystem(SystemMsgHelper.inventoryEmpty());
        } else if (inv.size() == 1) {
            Item it = inv.get(0);
            outputController.printlnSystem(SystemMsgHelper.singleItemInInventory(it.getName(), it.getDescription()));
        } else {
            String names = inv.stream().map(Item::getName).collect(Collectors.joining(", "));
            outputController.printlnSystem(SystemMsgHelper.multipleItemsInInventory(names));
        }
    }

    private void handleGo(String direction) {
        String full = CommandAliasHelper.normalizeDirection(direction);
        MoveResult result = gameLogic.move(full);

        if (result.success()) {
            outputController.printlnSystem(SystemMsgHelper.moveMessage(full));
            enterRoom(gameLogic.getCurrentRoom());
            return;
        }

        List<String> exits = result.availableExits();
        if (exits.isEmpty()) {
            outputController.printlnGM(GMHelper.deadEndMessage(full));
        } else {
            handleDeadEnd();
        }
    }

    private void handlePickup(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            outputController.printlnSystem(SystemMsgHelper.askWhichItemToPickup());
            return;
        }

        Item it = gameLogic.pickupItem(itemName);
        if (it == null) {
            outputController.printlnSystem(SystemMsgHelper.itemNotHere(itemName));
        } else {
            outputController.printlnGM(GMHelper.randomPickupHint(it));
        }
    }

    private void handleDrop(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            outputController.printlnSystem(SystemMsgHelper.askWhichItemToDrop());
            return;
        }

        Item it = gameLogic.dropItem(itemName);
        if (it == null) {
            outputController.printlnSystem(SystemMsgHelper.itemNotInInventory(itemName));
        } else {
            outputController.printlnGM(GMHelper.randomDropHint(it));
        }
    }

    private void showExitsHint() {
        outputController.printlnGM(GMHelper.randomHintAttempt());

        Set<String> exits = gameLogic.getAvailableExits();
        if (exits.isEmpty()) {
            outputController.printlnSystem(SystemMsgHelper.noVisibleExits());
        } else if (exits.size() == 1) {
            outputController.printlnSystem(SystemMsgHelper.singleVisibleExit(exits.iterator().next()));
        } else {
            outputController.printlnSystem(SystemMsgHelper.multipleVisibleExits(String.join(", ", exits)));
        }
    }

    private void handleDeadEnd() {
        String exits = String.join(", ", gameLogic.getAvailableExits());
        outputController.printlnGM(GMHelper.randomDeadEndHint(exits));
    }

    private void handleUnknownCommand() {
        outputController.printlnGM(GMHelper.randomUnknownCommandHint());
    }

    // ---------- button delegates ----------
    @FXML private void onNorth() { actions.north(); }
    @FXML private void onSouth() { actions.south(); }
    @FXML private void onEast()  { actions.east(); }
    @FXML private void onWest()  { actions.west(); }

    @FXML private void onLook() { actions.look(); }
    @FXML private void onInventory() { actions.inventory(); }
    @FXML private void onHint() { actions.hint(); }

    @FXML private void onPickup() { actions.pickup(); }
    @FXML private void onDrop() { actions.drop(); }

    // TODO implement
    @FXML private void onQuest() { }
    @FXML private void onStatus() { }
    @FXML private void onUse() { }
    @FXML private void onMap() { }
}