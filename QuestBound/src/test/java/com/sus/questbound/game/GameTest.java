package com.sus.questbound.game;

import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        Player player = new Player("Hero");
        game = new Game(player);
    }

    @Test
    void testStartingRoom() {
        Room start = game.getCurrentRoom();
        assertEquals("Entrance Hall", start.getName());
    }

    @Test
    void testMoveNorth() {
        MoveResult result = game.move("north");
        assertTrue(result.success());
        assertEquals("Corridor", game.getCurrentRoom().getName());
        assertTrue(result.availableExits().contains("south"));
    }

    @Test
    void testMoveWithAlias() {
        MoveResult result = game.move("n"); // alias for north
        assertTrue(result.success());
        assertEquals("Corridor", game.getCurrentRoom().getName());

        result = game.move("e"); // Corridor -> Armory
        assertTrue(result.success());
        assertEquals("Armory", game.getCurrentRoom().getName());
    }

    @Test
    void testMoveInvalidDirection() {
        MoveResult result = game.move("west"); // No exit from Entrance
        assertFalse(result.success());
        assertEquals("Entrance Hall", game.getCurrentRoom().getName());
        assertTrue(result.availableExits().contains("north"));
    }

    @Test
    void testMoveBackAndForth() {
        game.move("north"); // Entrance -> Corridor
        assertEquals("Corridor", game.getCurrentRoom().getName());

        MoveResult back = game.move("south"); // Corridor -> Entrance
        assertTrue(back.success());
        assertEquals("Entrance Hall", game.getCurrentRoom().getName());
    }

    @Test
    void testAvailableExits() {
        MoveResult result = game.move("north");
        Room corridor = result.newRoom();
        Set<String> exits = corridor.getAvailableExits();
        assertTrue(exits.contains("south"));
        assertTrue(exits.contains("east"));
        assertTrue(exits.contains("west"));
    }
}
