package com.sus.questbound.game;

import com.sus.questbound.game.world.FixedWorldGenerator;
import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player;
    private GameLogicController gameLogic;

    @BeforeEach
    void setUp() {
        player = new Player("Hero");
        game = new Game(player, new FixedWorldGenerator());
        gameLogic = new GameLogicController(game);
    }

    // ---------- World & movement ----------
    @Test
    void playerStartsInEntranceHall() {
        Room start = game.getCurrentRoom();

        assertEquals("Entrance Hall", start.getName());
    }

    @Test
    void movingNorthFromEntranceLeadsToCorridor() {
        // Act
        MoveResult result = game.move("north");

        // Assert
        assertTrue(result.success(), "Move north should succeed");
        assertEquals("Corridor", game.getCurrentRoom().getName());
        assertTrue(result.availableExits().contains("south"));
    }

    @Test
    void movementAliasesWorkCorrectly() {
        MoveResult north = game.move("n");
        assertTrue(north.success());
        assertEquals("Corridor", game.getCurrentRoom().getName());

        MoveResult east = game.move("e");
        assertTrue(east.success());
        assertEquals("Armory", game.getCurrentRoom().getName());
    }

    @Test
    void invalidDirectionDoesNotChangeRoom() {
        Room start = game.getCurrentRoom();

        MoveResult result = game.move("west");

        assertFalse(result.success(), "Move west should fail");
        assertSame(start, game.getCurrentRoom(), "Player should remain in same room");
        assertTrue(result.availableExits().contains("north"));
    }

    @Test
    void playerCanMoveBackAndForthBetweenRooms() {
        game.move("north");
        assertEquals("Corridor", game.getCurrentRoom().getName());

        MoveResult back = game.move("south");
        assertTrue(back.success());
        assertEquals("Entrance Hall", game.getCurrentRoom().getName());
    }

    @Test
    void corridorHasExpectedExits() {
        MoveResult result = game.move("north");
        Room corridor = result.newRoom();

        Set<String> exits = corridor.getAvailableExits();

        assertTrue(exits.contains("south"));
        assertTrue(exits.contains("east"));
        assertTrue(exits.contains("west"));
    }

    // ---------- Items & inventory ----------
    @Test
    void playerCanPickUpAndDropItem() {
        // Arrange
        Room startingRoom = game.getCurrentRoom();

        Item item = startingRoom.getItems().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Starting room should contain at least one item"));

        // Act – pick up
        Item pickedUpItem = gameLogic.pickupItem(item.name());

        // Assert – pickup
        assertNotNull(pickedUpItem, "pickupItem should return the picked up item");
        assertEquals(item, pickedUpItem, "Picked up item should be the expected item");

        assertTrue(player.getInventory().contains(item), "Player inventory should contain the item");
        assertFalse(startingRoom.getItems().contains(item), "Room should no longer contain the item");

        // Act – drop
        Item droppedItem = gameLogic.dropItem(item.name());

        // Assert – drop
        assertNotNull(droppedItem, "dropItem should return the dropped item");
        assertEquals(item, droppedItem, "Dropped item should be the same item");

        assertFalse(player.getInventory().contains(item), "Item should be removed from player inventory");
        assertTrue(startingRoom.getItems().contains(item), "Item should be added back to the room");
    }
}
