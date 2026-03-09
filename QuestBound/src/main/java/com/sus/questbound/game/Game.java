package com.sus.questbound.game;

import com.sus.questbound.game.world.World;
import com.sus.questbound.game.world.WorldGenerator;
import com.sus.questbound.model.*;

import java.util.List;

public class Game {

    private final Player player;
    private Room currentRoom;
    private final World world;

    public Game(Player player, WorldGenerator generator) {
        this.player = player;
        world = generator.generate();
        this.currentRoom = world.getStartRoom();
        this.currentRoom.setVisited(true);
    }

    public World getWorld() {
        return world;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public MoveResult move(Direction direction) {
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom != null) {
            currentRoom = nextRoom;
            currentRoom.setVisited(true);

            return new MoveResult(true, currentRoom, List.copyOf(currentRoom.getAvailableExits()));
        } else {
            return new MoveResult(false, currentRoom, List.copyOf(currentRoom.getAvailableExits()));
        }
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
