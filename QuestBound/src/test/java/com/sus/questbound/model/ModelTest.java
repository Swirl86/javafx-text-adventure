package com.sus.questbound.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ModelTest {

    @Test
    void testPlayerAndRoomWithItems() {
        // Arrange
        Player player = new Player("Hero");
        Room startingRoom = new Room("Entrance Hall", "A grand hall with high ceilings.");
        startingRoom.addItem(Item.KEY);
        startingRoom.addItem(Item.LANTERN);

        player.addItem(Item.MAP);
        player.addItem(Item.TORCH);

        // Act
        String playerName = player.getName();
        List<Item> playerInventory = player.getInventory();
        String roomName = startingRoom.getName();
        String roomDesc = startingRoom.getDescription();
        List<Item> roomItems = startingRoom.getItems();

        // Assert
        assertEquals("Hero", playerName, "Player name should match");
        assertEquals("Entrance Hall", roomName, "Room name should match");
        assertEquals("A grand hall with high ceilings.", roomDesc, "Room description should match");

        assertEquals(2, roomItems.size(), "Room should have 2 items");
        assertTrue(roomItems.contains(Item.KEY), "Room should contain the Key");
        assertTrue(roomItems.contains(Item.LANTERN), "Room should contain the Lantern");

        assertEquals(2, playerInventory.size(), "Player should have 2 items");
        assertTrue(playerInventory.contains(Item.MAP), "Player should have the Map");
        assertTrue(playerInventory.contains(Item.TORCH), "Player should have the Torch");
    }
}
