package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.model.Room;
import com.sus.questbound.game.world.GameWorld;
import com.sus.questbound.game.world.config.DungeonGenerationConfig;

import java.util.Map;
import java.util.Random;

/**
 * Generates a dungeon layout based on the provided configuration.
 * Used by Game to construct the initial world state.
 */
public class RandomDungeonGenerator implements DungeonGenerator {

    private final DungeonLayoutGenerator layout = new DungeonLayoutGenerator();
    private final DungeonItemPlacer itemPlacer = new DungeonItemPlacer();
    private final DungeonExitPlacer exitPlacer = new DungeonExitPlacer();
    private final KeyItemPlacer keyPlacer = new KeyItemPlacer();

    private final Random random = new Random();
    private final DungeonGenerationConfig config;

    public RandomDungeonGenerator(DungeonGenerationConfig config) {
        this.config = config;
    }

    @Override
    public GameWorld generate() {

        int roomCount = random.nextInt(
                config.maxRooms() - config.minRooms() + 1
        ) + config.minRooms();

        Map<String, Room> map = layout.generateRooms(roomCount);

        Room startRoom = map.get("0,0");

        itemPlacer.placeItems(map.values());
        exitPlacer.placeExit(map.values(), startRoom);
        keyPlacer.placeKey(map.values());

        return new GameWorld(startRoom);
    }
}