package com.sus.questbound.logic;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.MoveResult;
import com.sus.questbound.model.Direction;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import java.util.List;
import java.util.Optional;

public record GameLogicController(Game game) {

    // ---------- Player ----------
    public Player getPlayer() {
        return game.getPlayer();
    }

    public List<Item> getPlayerInventory() {
        return List.copyOf(game.getPlayer().getInventory());
    }

    // ---------- Room ----------
    public Room getCurrentRoom() {
        return game.getCurrentRoom();
    }

    public List<Item> getRoomItems() {
        return List.copyOf(game.getCurrentRoom().getItems());
    }

    public List<Item> getPickupableItems() {
        return game.getCurrentRoom().getItems().stream()
                .filter(Item::pickupable)
                .toList();
    }

    public List<Direction> getAvailableExits() {
        return List.copyOf(game.getCurrentRoom().getAvailableExits());
    }

    // ---------- Movement ----------
    public MoveResult move(Direction direction) {
        return game.move(direction);
    }

    // ---------- Pickup / Drop ----------

    /**
     * Attempts to pick up an item from the current room.
     *
     * @param itemName the name of the item to pick up
     * @return an Optional containing the picked-up Item if successful,
     *         or an empty Optional if the item does not exist or cannot be picked up
     */
    public Optional<Item> pickupItem(String itemName) {
        Item item = game.getCurrentRoom().getItemByName(itemName);

        if (item != null && game.pickupItem(itemName)) {
            return Optional.of(item);
        }

        return Optional.empty();
    }

    /**
     * Attempts to drop an item from the player's inventory.
     *
     * @param itemName the name of the item to drop
     * @return an Optional containing the dropped Item if successful,
     *         or an empty Optional if the item does not exist in the inventory
     */
    public Optional<Item> dropItem(String itemName) {
        Item item = game.getPlayer().getItemByName(itemName);

        if (item != null && game.dropItem(itemName)) {
            return Optional.of(item);
        }

        return Optional.empty();
    }
}
