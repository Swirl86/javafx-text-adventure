package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.data.item.ItemRepository;
import com.sus.questbound.game.model.Room;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class KeyItemPlacer {

    private final Random random = new Random();

    public void placeKey(Collection<Room> rooms) {
        List<Room> candidates = rooms.stream()
                .filter(r -> !r.isDungeonExit())
                .toList();

        Room keyRoom = candidates.get(random.nextInt(candidates.size()));
        keyRoom.addItem(ItemRepository.createDungeonExitKey());
    }
}

