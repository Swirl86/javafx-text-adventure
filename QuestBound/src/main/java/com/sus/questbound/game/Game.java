package com.sus.questbound.game;

import com.sus.questbound.game.engine.MoveResult;
import com.sus.questbound.game.model.Direction;
import com.sus.questbound.game.model.Item;
import com.sus.questbound.game.model.Player;
import com.sus.questbound.game.model.Room;
import com.sus.questbound.game.world.GameWorld;
import com.sus.questbound.game.world.generator.DungeonGenerator;

import java.util.List;

/**
 * Represents a single game session, containing the player, world, and dungeon generator.
 * Provides low-level state transitions such as movement and item interactions.
 */
public class Game {

    private final Player player;
    private Room currentRoom;
    private final GameWorld gameWorld;

    public Game(Player player, DungeonGenerator generator) {
        this.player = player;
        gameWorld = generator.generate();
        this.currentRoom = gameWorld.getStartRoom();
        this.currentRoom.setVisited(true);
    }

    public GameWorld getWorld() {
        return gameWorld;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public MoveResult move(Direction direction) {
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom != null) {
            enterRoom(nextRoom);

            return new MoveResult(true, currentRoom, List.copyOf(currentRoom.getAvailableExits()));
        } else {
            return new MoveResult(false, currentRoom, List.copyOf(currentRoom.getAvailableExits()));
        }
    }

    private void enterRoom(Room room) {
        currentRoom = room;
        currentRoom.setVisited(true);
    }

    public Player getPlayer() {
        return player;
    }

    public boolean pickupItem(String itemName) {
        Item item = currentRoom.getItemByName(itemName);
        return item != null && player.addItem(item) && currentRoom.removeItem(item);
    }

    public boolean dropItem(String itemName) {
        Item item = player.getItemByName(itemName);
        return item != null && player.removeItem(item) && currentRoom.addItem(item);
    }
}
