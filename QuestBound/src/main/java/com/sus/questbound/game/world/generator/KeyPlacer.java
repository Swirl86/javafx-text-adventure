package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.library.item.ItemLibrary;
import com.sus.questbound.model.Room;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KeyPlacer {

    private final Random random = new Random();

    public void placeKey(Collection<Room> rooms) {
        List<Room> candidates = rooms.stream()
                .filter(r -> !r.isDungeonExit())
                .toList();

        Room keyRoom = candidates.get(random.nextInt(candidates.size()));
        keyRoom.addItem(ItemLibrary.createDungeonExitKey());
    }
}

