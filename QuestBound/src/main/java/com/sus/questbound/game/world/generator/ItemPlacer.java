package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.library.item.ItemLibrary;
import com.sus.questbound.model.Room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ItemPlacer {

    private final Random random = new Random();

    public void placeItems(Collection<Room> rooms) {
        int itemCount = random.nextInt(rooms.size()) + 3;
        List<Room> list = new ArrayList<>(rooms);

        for (int i = 0; i < itemCount; i++) {
            list.get(random.nextInt(list.size()))
                    .addItem(ItemLibrary.createRandomItem());
        }
    }
}
