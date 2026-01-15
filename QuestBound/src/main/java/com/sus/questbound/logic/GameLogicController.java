package com.sus.questbound.logic;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.MoveResult;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record GameLogicController(Game game) {

    // ---------- Player ----------
    public Player getPlayer() {
        return game.getPlayer();
    }

    public List<Item> getPlayerInventory() {
        return new ArrayList<>(game.getPlayer().getInventory());
    }

    // ---------- Room ----------
    public Room getCurrentRoom() {
        return game.getCurrentRoom();
    }

    public Set<String> getAvailableExits() {
        return game.getCurrentRoom().getAvailableExits();
    }

    // ---------- Movement ----------
    public MoveResult move(String direction) {
        return game.move(direction);
    }

    // ---------- Pickup / Drop ----------

    /**
     * Picks up an item from the room.
     * Returns item if successful, null otherwise.
     */
    public Item pickupItem(String itemName) {
        Item item = game.getCurrentRoom().getItemByName(itemName);
        if (item != null && game.pickupItem(itemName)) {
            return item;
        }
        return null;
    }

    /**
     * Drops an item from the player's inventory.
     * Returns item if successful, otherwise null.
     */
    public Item dropItem(String itemName) {
        Item item = game.getPlayer().getItemByName(itemName);
        if (item != null && game.dropItem(itemName)) {
            return item;
        }
        return null;
    }
}
