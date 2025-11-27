package com.sus.questbound.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ModelTest {

    @Test
    void testPlayerAndRoom() {
        // Arrange
        Player player = new Player("Hero");
        Room startingRoom = new Room("Entrance Hall", "A grand hall with high ceilings.");
        startingRoom.addItem("Key");

        // Act
        String playerName = player.getName();
        String roomName = startingRoom.getName();
        String roomDesc = startingRoom.getDescription();
        List<String> items = startingRoom.getItems();

        // Assert
        assertEquals("Hero", playerName, "Player name should match");
        assertEquals("Entrance Hall", roomName, "Room name should match");
        assertEquals("A grand hall with high ceilings.", roomDesc, "Room description should match");
        assertEquals(1, items.size(), "Room should have 1 item");
        assertTrue(items.contains("Key"), "Room should contain the Key");
    }
}
