package com.sus.questbound.logic;

import com.sus.questbound.game.Game;
import com.sus.questbound.game.MoveResult;
import com.sus.questbound.model.*;
import com.sus.questbound.model.view.MapExitView;
import com.sus.questbound.model.view.MapItemView;
import com.sus.questbound.model.view.MapRoomView;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                .filter(Item::isPickupable)
                .toList();
    }

    public List<Direction> getAvailableExits() {
        return List.copyOf(game.getCurrentRoom().getAvailableExits());
    }

    public Set<Room> getAllRooms() {
        return game.getWorld().getAllRooms();
    }

    // ---------- Map ----------
    public List<MapRoomView> getMapRooms() {
        Room playerRoom = getCurrentRoom();

        return getAllRooms().stream()
                .map(room -> {

                    var items = room.getItems().stream()
                            .map(item -> new MapItemView(
                                    item.name(),
                                    item.hasTag(ItemTags.FINAL_KEY.id()) ? "#0000FF" :   // blue
                                            item.isPickupable() ? "#00FF00" :            // green
                                                    "#FF0000"                            // red
                            ))
                            .toList();

                    return new MapRoomView(
                            room.getX(),
                            room.getY(),
                            room.getName(),
                            room.getDescription(),
                            room.isVisited(),
                            room.isDungeonExit(),
                            room == playerRoom,
                            items
                    );
                })
                .toList();
    }

    public List<MapExitView> getMapExits() {
        return getAllRooms().stream()
                .flatMap(room -> room.getExits().values().stream()
                        .map(exit -> new MapExitView(
                                room.getX(), room.getY(),
                                exit.getX(), exit.getY(),
                                room.isVisited() && exit.isVisited()
                        ))
                )
                .toList();
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
