package com.sus.questbound.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void playerHasNameAndEmptyInventoryOnCreation() {
        Player player = new Player("Hero");

        assertEquals("Hero", player.getName());
        assertTrue(player.getInventory().isEmpty(), "Inventory should be empty at start");
    }

    @Test
    void playerCanAddAndRemoveItemFromInventory() {
        Player player = new Player("Hero");
        Item key = new Item(
                "key_01",
                "Key",
                "A small rusty key",
                true,
                null,
                null,
                Set.of("key")
        );

        player.addItem(key);
        assertEquals(1, player.getInventory().size());
        assertTrue(player.getInventory().contains(key));

        player.removeItem(key);
        assertTrue(player.getInventory().isEmpty());
    }

    @Test
    void roomHasNameDescriptionAndNoItemsInitially() {
        Room room = new Room("Entrance Hall", "A grand hall with high ceilings.", 0, 0);

        assertEquals("Entrance Hall", room.getName());
        assertEquals("A grand hall with high ceilings.", room.getDescription());
        assertTrue(room.getItems().isEmpty());
    }

    @Test
    void roomCanAddAndRemoveItems() {
        Room room = new Room("Armory", "Old weapons line the walls", 1, 0);

        Item sword = new Item(
                "sword_01",
                "Sword",
                "A sharp blade",
                true,
                null,
                5,
                Set.of("weapon")
        );

        room.addItem(sword);
        assertEquals(List.of(sword), room.getItems());

        room.removeItem(sword);
        assertTrue(room.getItems().isEmpty());
    }

    @Test
    void itemHasTagsAndCanCheckForSpecificTag() {
        Item lantern = new Item(
                "lantern_01",
                "Lantern",
                "An old lantern",
                true,
                null,
                null,
                Set.of("light", "tool")
        );

        assertTrue(lantern.hasTag("light"));
        assertTrue(lantern.hasTag("tool"));
        assertFalse(lantern.hasTag("weapon"));
    }
}
