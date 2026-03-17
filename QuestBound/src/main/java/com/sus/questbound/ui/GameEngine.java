package com.sus.questbound.ui;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.engine.GameEventService;
import com.sus.questbound.game.engine.GameLogic;
import com.sus.questbound.game.engine.MoveResult;
import com.sus.questbound.game.model.*;
import com.sus.questbound.ui.view.MapExitView;
import com.sus.questbound.ui.view.MapRoomView;

import java.util.List;
import java.util.Optional;

/**
 * High-level facade that exposes game functionality to the UI layer.
 * Delegates all game operations to GameLogic and GameEventService,
 * providing a clean and UI-friendly API for interacting with the game world.
 */
public class GameEngine {

    private final GameLogic logic;
    private final GameEventService events;

    public GameEngine(Game game) {
        this.logic = new GameLogic(game);
        this.events = new GameEventService();
    }

    // ----------- Player & World Access -----------
    public Player getPlayer() {
        return logic.getPlayer();
    }

    public Room getCurrentRoom() {
        return logic.getCurrentRoom();
    }

    public List<Item> getRoomItems() {
        return logic.getRoomItems();
    }

    public List<Item> getPlayerInventory() {
        return logic.getPlayerInventory();
    }

    public List<Direction> getAvailableExits() {
        return logic.getAvailableExits();
    }

    public List<Item> getPickupableItems() {
        return logic.getPickupableItems();
    }

    // ----------- Map Access -----------
    public List<MapRoomView> getMapRooms() {
        return logic.getMapRooms();
    }

    public List<MapExitView> getMapExits() {
        return logic.getMapExits();
    }

    // ----------- Game Actions -----------
    public MoveResult move(Direction direction) {
        return logic.move(direction);
    }

    public Optional<Item> pickupItem(String name) {
        return logic.pickupItem(name);
    }

    public Optional<Item> dropItem(String name) {
        return logic.dropItem(name);
    }

    public Optional<String> triggerEnterRoomEvent(Room room) {
        return events.onEnterRoom(room, logic.getPlayer());
    }
}