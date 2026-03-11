package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.model.Room;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DungeonExitPlacer {

    private final Random random = new Random();

    public void placeExit(Collection<Room> rooms, Room startRoom) {
        List<Room> candidates = rooms.stream()
                .filter(r -> r != startRoom)
                .toList();

        Room exitRoom = candidates.get(random.nextInt(candidates.size()));
        exitRoom.setDungeonExit(true);
    }
}
