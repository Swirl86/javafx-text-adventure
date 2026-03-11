package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.world.World;
import com.sus.questbound.game.world.WorldGenerator;
import com.sus.questbound.game.world.config.WorldGenerationConfig;
import com.sus.questbound.model.Room;

import java.util.*;

public class RandomWorldGenerator implements WorldGenerator {

    private final RoomLayoutGenerator layout = new RoomLayoutGenerator();
    private final ItemPlacer itemPlacer = new ItemPlacer();
    private final DungeonExitPlacer exitPlacer = new DungeonExitPlacer();
    private final KeyPlacer keyPlacer = new KeyPlacer();

    private final Random random = new Random();
    private final WorldGenerationConfig config;

    public RandomWorldGenerator(WorldGenerationConfig config) {
        this.config = config;
    }

    @Override
    public World generate() {

        int roomCount = random.nextInt(
                config.maxRooms() - config.minRooms() + 1
        ) + config.minRooms();

        Map<String, Room> map = layout.generateRooms(roomCount);

        Room startRoom = map.get("0,0");

        itemPlacer.placeItems(map.values());
        exitPlacer.placeExit(map.values(), startRoom);
        keyPlacer.placeKey(map.values());

        return new World(startRoom);
    }
}