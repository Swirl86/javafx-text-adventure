package com.sus.questbound.game;

import com.sus.questbound.util.CommandAliasHelper;
import com.sus.questbound.model.*;

import java.util.List;

public class Game {

    private final Player player;
    private Room currentRoom;

    public Game(Player player) {
        this.player = player;
        setupWorld();
    }

    private void setupWorld() {

        Room entrance = new Room("Entrance Hall",
                "A large entry hall with cold stone walls.");
        Room corridor = new Room("Corridor",
                "A narrow corridor with flickering torches.");
        Room armory = new Room("Armory",
                "A dusty armory with broken weapons.");
        Room library = new Room("Library",
                "Ancient books line the walls. The air smells of dust.");
        Room shrine = new Room("Shrine",
                "A quiet shrine with a glowing altar.");

        // Items
        entrance.addItem(Item.LANTERN);
        armory.addItem(Item.SWORD);
        library.addItem(Item.OLD_BOOK);

        // Exits
        connectRooms(entrance, "north", corridor, "south");
        connectRooms(corridor, "east", armory, "west");
        connectRooms(corridor, "west", library, "east");
        connectRooms(library, "north", shrine, "south");

        // Starting room
        currentRoom = entrance;
    }

    private void connectRooms(Room room1, String dir1to2, Room room2, String dir2to1) {
        room1.setExit(dir1to2, room2);
        room2.setExit(dir2to1, room1);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public MoveResult move(String direction) {
        direction = CommandAliasHelper.normalizeDirection(direction);
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom != null) {
            currentRoom = nextRoom;
            return new MoveResult(true, currentRoom, List.copyOf(currentRoom.getAvailableExits()));
        } else {
            return new MoveResult(false, currentRoom, List.copyOf(currentRoom.getAvailableExits()));
        }
    }

    public Player getPlayer() {
        return player;
    }
}
