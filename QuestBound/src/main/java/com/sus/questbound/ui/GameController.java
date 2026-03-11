package com.sus.questbound.ui;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.data.item.ItemRepository;
import com.sus.questbound.game.engine.GameEventService;
import com.sus.questbound.game.engine.GameLogic;
import com.sus.questbound.game.engine.MoveResult;
import com.sus.questbound.game.model.*;
import com.sus.questbound.game.world.config.DungeonGenerationConfig;
import com.sus.questbound.game.world.generator.FixedDungeonGenerator;
import com.sus.questbound.game.world.generator.RandomDungeonGenerator;
import com.sus.questbound.util.GMMsgHelper;
import com.sus.questbound.util.OutputFormatHelper;
import com.sus.questbound.util.PlayerMsgHelper;
import com.sus.questbound.util.SystemMsgHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.stream.Collectors;

public class GameController {

    @FXML private TextFlow outputFlow;
    @FXML private ScrollPane outputScroll;

    private GameLogic gameLogic;
    private GameEventService gameEventService;
    private PlayerActionController actions;
    private GameOutputController outputController;

    // ---------- lifecycle ----------
    @FXML
    public void initialize() {
        Player player = new Player("Hero");
        player.addItem(ItemRepository.createItemWithTag(ItemTags.NAVIGATION.id()));
        player.addItem(ItemRepository.createItemWithTag(ItemTags.LIGHT.id()));

        var config = new DungeonGenerationConfig(6, 12);

        // Fixed world for testing
        Game game1 = new Game(player, new FixedDungeonGenerator(config));

        // Random world with config
        Game game = new Game(player, new RandomDungeonGenerator(config));

        gameLogic = new GameLogic(game);
        gameEventService = new GameEventService();

        outputController = new GameOutputController(outputFlow, outputScroll);

        actions = new PlayerActionController(
                gameLogic,
                this::executeAction,
                outputController
        );

        outputController.println(SystemMsgHelper.randomWelcome(gameLogic.getPlayer()), MsgType.SYSTEM);
        outputController.println(SystemMsgHelper.enterRoom(gameLogic.getCurrentRoom()), MsgType.SYSTEM);
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
    private void handleLook() {
        OutputFormatHelper.printCollectionWithDetails(
                outputController,
                gameLogic.getRoomItems(),
                MsgType.SYSTEM,
                SystemMsgHelper::nothingToSee,
                it -> SystemMsgHelper.singleItemInRoom(it.name(), it.description()),
                SystemMsgHelper::multipleItemsInRoom,
                Item::name
        );
    }

    private void handleInventory() {
        OutputFormatHelper.printCollectionWithDetails(
                outputController,
                gameLogic.getPlayerInventory(),
                MsgType.SYSTEM,
                SystemMsgHelper::inventoryEmpty,
                it -> SystemMsgHelper.singleItemInInventory(it.name(), it.description()),
                SystemMsgHelper::multipleItemsInInventory,
                Item::name
        );
    }

    private void handleGo(Direction direction) {
        MoveResult result = gameLogic.move(direction);
        String dirName = direction.label();

        if (result.success()) {
            Room newRoom = gameLogic.getCurrentRoom();

            String message = String.format(
                    "%s %s",
                    SystemMsgHelper.moveMessage(dirName),
                    SystemMsgHelper.enterRoom(newRoom)
            );

            outputController.println(message, MsgType.SYSTEM);

            gameEventService
                    .onEnterRoom(newRoom, gameLogic.getPlayer())
                    .ifPresent(msg ->
                            outputController.println(msg, MsgType.GM)
                    );
            return;
        }

        List<Direction> exits = result.availableExits();

        if (exits.isEmpty()) {
            outputController.println(GMMsgHelper.deadEndDirectional(dirName), MsgType.GM);
        } else {
            printDeadEnd(exits);
        }
    }

    private void showExitsHint() {
        outputController.println(GMMsgHelper.hintAttempt(), MsgType.GM);

        OutputFormatHelper.printCollectionWithDetails(
                outputController,
                gameLogic.getAvailableExits(),
                MsgType.SYSTEM,
                SystemMsgHelper::noVisibleExits,
                dir -> SystemMsgHelper.singleVisibleExit(dir.label()),
                SystemMsgHelper::multipleVisibleExits,
                Direction::label
        );
    }

    private void printDeadEnd(List<Direction> exits) {
        String exitNames = exits.stream()
                .map(Direction::label)
                .collect(Collectors.joining(", "));

        outputController.println(GMMsgHelper.deadEnd(exitNames), MsgType.GM);
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
    @FXML private void onMap() { WorldMapDialog.show(gameLogic); }

    // TODO implement
    @FXML private void onQuest() { }
    @FXML private void onStatus() { }
    @FXML private void onUse() { }
}