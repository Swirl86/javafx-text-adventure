package com.sus.questbound.game.world;

import com.sus.questbound.game.library.item.ItemLibrary;
import com.sus.questbound.game.library.room.RoomLibrary;
import com.sus.questbound.model.Direction;
import com.sus.questbound.model.Room;

import java.util.*;

public class RandomWorldGenerator implements WorldGenerator {

    private static final Random RANDOM = new Random();
    private static final int ROOM_COUNT = 8;

    @Override
    public World generate() {

        Map<String, Room> map = new HashMap<>();

        Room startRoom = RoomLibrary.createStartRoom(0, 0);
        map.put(key(0, 0), startRoom);

        Room current = startRoom;

        for (int i = 1; i < ROOM_COUNT; i++) {

            List<Direction> dirs = new ArrayList<>(
                    Arrays.stream(Direction.values())
                            .filter(Direction::isPlanar)
                            .toList()
            );
            Collections.shuffle(dirs);

            boolean placed = false;

            for (Direction dir : dirs) {
                int nx = current.getX() + dir.dx;
                int ny = current.getY() + dir.dy;

                if (map.containsKey(key(nx, ny))) {
                    continue;
                }

                Room next = RoomLibrary.createRandomRoom(nx, ny);

                current.setExit(dir, next);
                next.setExit(dir.opposite(), current);

                map.put(key(nx, ny), next);

                current = next;
                placed = true;
                break;
            }

            if (!placed) {
                current = new ArrayList<>(map.values())
                        .get(RANDOM.nextInt(map.size()));
                i--;
            }
        }

        placeItemsRandomly(map.values());
        return new World(startRoom);
    }

    private void placeItemsRandomly(Collection<Room> rooms) {
        int itemCount = RANDOM.nextInt(rooms.size()) + 3;
        List<Room> list = new ArrayList<>(rooms);

        for (int i = 0; i < itemCount; i++) {
            list.get(RANDOM.nextInt(list.size()))
                    .addItem(ItemLibrary.createRandomItem());
        }
    }

    private String key(int x, int y) {
        return x + "," + y;
    }
}