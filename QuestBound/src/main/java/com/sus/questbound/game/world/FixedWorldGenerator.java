package com.sus.questbound.game.world;

import com.sus.questbound.game.library.item.ItemLibrary;
import com.sus.questbound.model.Room;

/**
 * Creates a static, predictable world for testing and debugging.
 */
public class FixedWorldGenerator implements WorldGenerator {

    @Override
    public World generate() {

        Room entrance = new Room("Entrance Hall", "A large entry hall with cold stone walls.");
        Room corridor = new Room("Corridor", "A narrow corridor with flickering torches.");
        Room armory = new Room("Armory", "A dusty armory with broken weapons.");
        Room library = new Room("Library", "Ancient books line the walls. The air smells of dust.");
        Room shrine = new Room("Shrine", "A quiet shrine with a glowing altar.");


        entrance.addItem(ItemLibrary.createItemWithTag("light"));
        armory.addItem(ItemLibrary.createItemWithTag("weapon"));
        library.addItem(ItemLibrary.createItemWithTag("lore"));
        shrine.addItem(ItemLibrary.createItemWithTag("quest"));


        connectRooms(entrance, "north", corridor, "south");
        connectRooms(corridor, "east", armory, "west");
        connectRooms(corridor, "west", library, "east");
        connectRooms(library, "north", shrine, "south");


        return new World(entrance);
    }

    private void connectRooms(Room room1, String dir1to2, Room room2, String dir2to1) {
        room1.setExit(dir1to2, room2);
        room2.setExit(dir2to1, room1);
    }
}