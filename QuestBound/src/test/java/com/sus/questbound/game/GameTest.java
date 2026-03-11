package com.sus.questbound.game;

import com.sus.questbound.game.engine.GameLogic;
import com.sus.questbound.game.engine.MoveResult;
import com.sus.questbound.game.model.*;
import com.sus.questbound.game.world.config.DungeonGenerationConfig;
import com.sus.questbound.game.world.generator.FixedDungeonGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player;
    private GameLogic gameLogic;

    @BeforeEach
    void setUp() {
        player = new Player("Hero");

        // Config is unused by FixedDungeonGenerator, but required for constructor
        DungeonGenerationConfig config = new DungeonGenerationConfig(1, 1);

        game = new Game(player, new FixedDungeonGenerator(config));
        gameLogic = new GameLogic(game);
    }

    // ---------- World & movement ----------
    @Test
    void playerStartsInEntranceHall() {
        Room start = game.getCurrentRoom();
        assertNotNull(start, "Game should always have a starting room");
        assertEquals("Entrance Hall", start.getName(), "Player should start in Entrance Hall");
        assertTrue(start.isVisited(), "Starting room should be marked as visited");
    }

    @Test
    void movingNorthFromEntranceLeadsToCorridor() {
        MoveResult result = gameLogic.move(Direction.NORTH);

        assertAll(
                () -> assertTrue(result.success(), "Move north should succeed"),
                () -> assertEquals("Corridor", game.getCurrentRoom().getName(), "Player should now be in Corridor"),
                () -> assertTrue(result.availableExits().contains(Direction.SOUTH), "Corridor should have a south exit"),
                () -> assertTrue(game.getCurrentRoom().isVisited(), "Corridor should be marked as visited")
        );
    }

    @Test
    void movementAliasesWorkCorrectly() {
        gameLogic.move(Direction.NORTH);
        assertEquals("Corridor", game.getCurrentRoom().getName());

        MoveResult east = gameLogic.move(Direction.EAST);

        assertAll(
                () -> assertTrue(east.success(), "Move east should succeed"),
                () -> assertEquals("Armory", game.getCurrentRoom().getName(), "Player should now be in Armory")
        );
    }

    @Test
    void invalidDirectionDoesNotChangeRoom() {
        Room start = game.getCurrentRoom();

        MoveResult result = gameLogic.move(Direction.WEST);

        assertAll(
                () -> assertFalse(result.success(), "Move west should fail"),
                () -> assertSame(start, game.getCurrentRoom(), "Player should remain in the same room"),
                () -> assertTrue(result.availableExits().contains(Direction.NORTH), "Entrance should have a north exit")
        );
    }

    @Test
    void playerCanMoveBackAndForthBetweenRooms() {
        gameLogic.move(Direction.NORTH);
        assertEquals("Corridor", game.getCurrentRoom().getName());

        MoveResult back = gameLogic.move(Direction.SOUTH);

        assertAll(
                () -> assertTrue(back.success(), "Move south should succeed"),
                () -> assertEquals("Entrance Hall", game.getCurrentRoom().getName(), "Player should return to Entrance Hall")
        );
    }

    @Test
    void corridorHasExpectedExits() {
        MoveResult result = gameLogic.move(Direction.NORTH);
        Room corridor = result.newRoom();

        Set<Direction> exits = corridor.getAvailableExits();

        assertAll(
                () -> assertTrue(exits.contains(Direction.SOUTH), "Corridor should have a south exit"),
                () -> assertTrue(exits.contains(Direction.EAST), "Corridor should have an east exit"),
                () -> assertTrue(exits.contains(Direction.WEST), "Corridor should have a west exit")
        );
    }

    // ---------- Items & inventory ----------
    @Test
    void playerCanPickUpAndDropItem() {
        Room startingRoom = game.getCurrentRoom();

        Item item = startingRoom.getItems().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("Starting room should contain at least one item"));

        // Pick up
        Item pickedUpItem = gameLogic.pickupItem(item.name())
                .orElseThrow(() -> new AssertionError("pickupItem should return an item"));

        assertAll(
                () -> assertEquals(item, pickedUpItem, "Picked up item should match expected item"),
                () -> assertTrue(player.getInventory().contains(item), "Player inventory should contain the item"),
                () -> assertFalse(startingRoom.getItems().contains(item), "Room should no longer contain the item")
        );

        // Drop
        Item droppedItem = gameLogic.dropItem(item.name())
                .orElseThrow(() -> new AssertionError("dropItem should return an item"));

        assertAll(
                () -> assertEquals(item, droppedItem, "Dropped item should be the same item"),
                () -> assertFalse(player.getInventory().contains(item), "Item should be removed from inventory"),
                () -> assertTrue(startingRoom.getItems().contains(item), "Item should be placed back in the room")
        );
    }

    // ---------- Fixed world structure ----------
    @Test
    void fixedWorldHasDungeonExitInArmory() {
        gameLogic.move(Direction.NORTH); // Corridor
        gameLogic.move(Direction.EAST);  // Armory

        Room armory = game.getCurrentRoom();

        assertTrue(armory.isDungeonExit(), "Armory should be marked as dungeon exit");
    }

    @Test
    void fixedWorldHasKeyInCorridor() {
        gameLogic.move(Direction.NORTH); // Corridor

        Room corridor = game.getCurrentRoom();

        boolean hasKey = corridor.getItems().stream()
                .anyMatch(i -> i.tags().contains(ItemTags.FINAL_KEY.id()));

        assertTrue(hasKey, "Corridor should contain the FINAL_KEY item");
    }
}
