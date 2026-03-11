package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.data.item.ItemRepository;
import com.sus.questbound.game.model.Direction;
import com.sus.questbound.game.model.ItemTags;
import com.sus.questbound.game.model.Room;
import com.sus.questbound.game.world.GameWorld;
import com.sus.questbound.game.world.config.DungeonGenerationConfig;

/**
 * Generates a static, predictable world for testing and debugging.
 * Accepts a DungeonGenerationConfig for consistency with other generators,
 * but ignores it since the world layout is fixed and does not use configuration.
 */
public record FixedDungeonGenerator(DungeonGenerationConfig config) implements DungeonGenerator {

    @Override
    public GameWorld generate() {

        /*
            (0,2)  Shrine
                    |
         (-1,1) Library -- (0,1) Corridor -- (1,1) Armory
                                |
                        (0,0) Entrance
         */

        Room entrance = new Room("Entrance Hall", "A large entry hall with cold stone walls.", 0, 0);
        Room corridor = new Room("Corridor", "A narrow corridor with flickering torches.", 0, 1);
        Room armory = new Room("Armory", "A dusty armory with broken weapons.", 1, 1);
        armory.setDungeonExit(true);
        Room library = new Room("Library", "Ancient books line the walls. The air smells of dust.", -1, 1);
        Room shrine = new Room("Shrine", "A quiet shrine with a glowing altar.", 0, 2);

        // ---------- items ----------
        entrance.addItem(ItemRepository.createItemWithTag(ItemTags.LIGHT.id()));
        entrance.addItem(ItemRepository.createItemWithTag(ItemTags.BIG_DECORATION.id()));
        corridor.addItem(ItemRepository.createDungeonExitKey());
        armory.addItem(ItemRepository.createItemWithTag(ItemTags.WEAPON.id()));
        library.addItem(ItemRepository.createItemWithTag(ItemTags.LORE.id()));
        shrine.addItem(ItemRepository.createItemWithTag(ItemTags.QUEST.id()));

        // ---------- connect rooms ----------
        connect(entrance, Direction.NORTH, corridor);
        connect(corridor, Direction.EAST, armory);
        connect(corridor, Direction.WEST, library);
        connect(library, Direction.NORTH, shrine);

        return new GameWorld(entrance);
    }

    private void connect(Room from, Direction dir, Room to) {
        from.setExit(dir, to);
        to.setExit(dir.opposite(), from);
    }
}