package com.sus.questbound;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.MoveResult;
import com.sus.questbound.game.library.item.ItemLibrary;
import com.sus.questbound.game.world.FixedWorldGenerator;
import com.sus.questbound.game.world.RandomWorldGenerator;
import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.*;
import com.sus.questbound.ui.ActionController;
import com.sus.questbound.ui.OutputController;
import com.sus.questbound.util.GMMsgHelper;
import com.sus.questbound.util.PlayerMsgHelper;
import com.sus.questbound.util.SystemMsgHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TextFlow outputFlow;
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
                this::executeAction,
                outputController
        );

        outputController.println(SystemMsgHelper.randomWelcome(gameLogic.getPlayer()), MsgType.SYSTEM);
        enterRoom(gameLogic.getCurrentRoom());
    }

    private void executeAction(Action action, Direction direction) {

        outputController.println(PlayerMsgHelper.getPlayerMsg(action, direction), MsgType.PLAYER);

        switch (action) {
            case LOOK -> handleLook();
            case INVENTORY -> handleInventory();
            case HINT -> showExitsHint();
            case GO -> {
                if (direction != null) {
                    handleGo(direction);
                }
            }
            default -> outputController.println(PlayerMsgHelper.getDefaultMessage(), MsgType.PLAYER);
        }
    }

    // ---------- game gameLogic ----------
    private void enterRoom(Room room) {
        outputController.println(SystemMsgHelper.enterRoom(room), MsgType.SYSTEM);
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

    private void handleGo(Direction direction) {
        MoveResult result = gameLogic.move(direction);
        String dirName = direction.label();

        if (result.success()) {
            Room newRoom = gameLogic.getCurrentRoom();
            outputController.println(SystemMsgHelper.moveMessage(dirName), MsgType.SYSTEM );
            enterRoom(newRoom);
            if (newRoom.isDungeonExit()) {
                if (gameLogic.getPlayer().hasItemWithTag(ItemTags.FINAL_KEY.id())) {
                    outputController.println(GMMsgHelper.dungeonExitWithKey(), MsgType.GM);
                    // TODO: expand endgame
                } else {
                    outputController.println(GMMsgHelper.dungeonExitWithoutKey(), MsgType.GM);
                }
            } else if (newRoom.containsItemWithTag(ItemTags.FINAL_KEY.id())) {
                outputController.println(GMMsgHelper.finalKeyPresenceHint(), MsgType.GM);
            }
            return;
        }

        if (result.availableExits().isEmpty()) {
            outputController.println(GMMsgHelper.deadEndDirectional(dirName), MsgType.GM);
        } else {
            handleDeadEnd();
        }
    }

    private void showExitsHint() {
        outputController.println(GMMsgHelper.hintAttempt(), MsgType.GM);

        Set<Direction> exits = gameLogic.getAvailableExits();
        if (exits.isEmpty()) {
            outputController.println(SystemMsgHelper.noVisibleExits(), MsgType.SYSTEM);
        } else if (exits.size() == 1) {
            Direction dir = exits.iterator().next();
            outputController.println(SystemMsgHelper.singleVisibleExit(dir.label()), MsgType.SYSTEM);
        } else {
            String exitNames = exits.stream()
                    .map(Direction::label)
                    .collect(Collectors.joining(", "));
            outputController.println(SystemMsgHelper.multipleVisibleExits(exitNames), MsgType.SYSTEM);
        }
    }

    private void handleDeadEnd() {
        String exits = gameLogic.getAvailableExits().stream()
                .map(Direction::label)
                .collect(Collectors.joining(", "));

        outputController.println(GMMsgHelper.deadEnd(exits), MsgType.GM);
    }

    // ---------- button delegates ----------
    @FXML private void onNorth() { executeAction(Action.GO, Direction.NORTH); }
    @FXML private void onSouth() { executeAction(Action.GO, Direction.SOUTH); }
    @FXML private void onEast()  { executeAction(Action.GO, Direction.EAST); }
    @FXML private void onWest()  { executeAction(Action.GO, Direction.WEST); }

    @FXML private void onLook() { executeAction(Action.LOOK, null); }
    @FXML private void onInventory() { executeAction(Action.INVENTORY, null); }
    @FXML private void onHint() { executeAction(Action.HINT, null); }
    @FXML private void onPickup() { actions.pickup(); }
    @FXML private void onDrop()   { actions.drop(); }

    // TODO implement
    @FXML private void onQuest() { }
    @FXML private void onStatus() { }
    @FXML private void onUse() { }
    @FXML private void onMap() { }
}