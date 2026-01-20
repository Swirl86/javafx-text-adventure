package com.sus.questbound;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.MoveResult;
import com.sus.questbound.game.command.CommandParser;
import com.sus.questbound.game.library.ItemLibrary;
import com.sus.questbound.game.world.FixedWorldGenerator;
import com.sus.questbound.game.world.RandomWorldGenerator;
import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.MsgType;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import com.sus.questbound.ui.ActionController;
import com.sus.questbound.ui.OutputController;
import com.sus.questbound.util.CommandAliasHelper;
import com.sus.questbound.util.GMHelper;
import com.sus.questbound.util.PlayerMsgHelper;
import com.sus.questbound.util.SystemMsgHelper;
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
        player.addItem(ItemLibrary.createItemWithTag("navigation"));
        player.addItem(ItemLibrary.createItemWithTag("light"));

        // Fixed world for testing
        Game game1 = new Game(player, new FixedWorldGenerator());
        // Random world
        Game game = new Game(player, new RandomWorldGenerator());

        gameLogic = new GameLogicController(game);

        outputController = new OutputController(outputFlow, outputScroll);

        actions = new ActionController(
                gameLogic,
                this::executeCommand,
                outputController
        );

        outputController.println(SystemMsgHelper.randomWelcome(gameLogic.getPlayer()), MsgType.SYSTEM);
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

        outputController.println(raw.toUpperCase(), MsgType.PLAYER);

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
        outputController.println(SystemMsgHelper.enterRoomMessage(room), MsgType.SYSTEM);
    }

    private void handleLook() {
        List<Item> items = gameLogic.getCurrentRoom().getItems();

        if (items.isEmpty()) {
            outputController.println(SystemMsgHelper.nothingToSee(), MsgType.SYSTEM);
        } else if (items.size() == 1) {
            Item it = items.get(0);
            outputController.println(SystemMsgHelper.singleItemInRoom(it.name(), it.description()), MsgType.SYSTEM);
        } else {
            String names = items.stream().map(Item::name).collect(Collectors.joining(", "));
            outputController.println(SystemMsgHelper.multipleItemsInRoom(names), MsgType.SYSTEM);
        }
    }

    private void handleInventory() {
        List<Item> inv = gameLogic.getPlayerInventory();

        if (inv.isEmpty()) {
            outputController.println(SystemMsgHelper.inventoryEmpty(), MsgType.SYSTEM);
        } else if (inv.size() == 1) {
            Item it = inv.get(0);
            outputController.println(SystemMsgHelper.singleItemInInventory(it.name(), it.description()), MsgType.SYSTEM);
        } else {
            String names = inv.stream().map(Item::name).collect(Collectors.joining(", "));
            outputController.println(SystemMsgHelper.multipleItemsInInventory(names), MsgType.SYSTEM);
        }
    }

    private void handleGo(String direction) {
        String full = CommandAliasHelper.normalizeDirection(direction);
        MoveResult result = gameLogic.move(full);

        if (result.success()) {
            outputController.println(SystemMsgHelper.moveMessage(full), MsgType.SYSTEM);
            enterRoom(gameLogic.getCurrentRoom());
            return;
        }

        List<String> exits = result.availableExits();
        if (exits.isEmpty()) {
            outputController.println(GMHelper.deadEndMessage(full), MsgType.GM);
        } else {
            handleDeadEnd();
        }
    }

    private void handlePickup(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            outputController.println(SystemMsgHelper.askWhichItemToPickup(), MsgType.SYSTEM);
            return;
        }

        Item it = gameLogic.pickupItem(itemName);
        if (it == null) {
            outputController.println(SystemMsgHelper.itemNotHere(itemName), MsgType.SYSTEM);
        } else {
            outputController.println(GMHelper.randomPickupHint(it), MsgType.GM);
        }
    }

    private void handleDrop(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            outputController.println(SystemMsgHelper.askWhichItemToDrop(), MsgType.SYSTEM);
            return;
        }

        Item it = gameLogic.dropItem(itemName);
        if (it == null) {
            outputController.println(SystemMsgHelper.itemNotInInventory(itemName), MsgType.SYSTEM);
        } else {
            outputController.println(GMHelper.randomDropHint(it), MsgType.GM);
        }
    }

    private void showExitsHint() {
        outputController.println(GMHelper.randomHintAttempt(), MsgType.GM);

        Set<String> exits = gameLogic.getAvailableExits();
        if (exits.isEmpty()) {
            outputController.println(SystemMsgHelper.noVisibleExits(), MsgType.SYSTEM);
        } else if (exits.size() == 1) {
            outputController.println(SystemMsgHelper.singleVisibleExit(exits.iterator().next()), MsgType.SYSTEM);
        } else {
            outputController.println(SystemMsgHelper.multipleVisibleExits(String.join(", ", exits)), MsgType.SYSTEM);
        }
    }

    private void handleDeadEnd() {
        String exits = String.join(", ", gameLogic.getAvailableExits());
        outputController.println(GMHelper.randomDeadEndHint(exits), MsgType.GM);
    }

    private void handleUnknownCommand() {
        outputController.println(GMHelper.randomUnknownCommandHint(), MsgType.GM);
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