package com.sus.questbound.game.world.generator;

import com.sus.questbound.game.data.room.RoomRepository;
import com.sus.questbound.game.model.Direction;
import com.sus.questbound.game.model.Room;

import java.util.*;
import java.util.stream.Collectors;

public class DungeonLayoutGenerator {

    private final Random random = new Random();

    public Map<String, Room> generateRooms(int roomCount) {
        Map<String, Room> map = new HashMap<>();

        Room start = RoomRepository.createStartRoom(0, 0);
        map.put(key(0, 0), start);

        Room current = start;

        for (int i = 1; i < roomCount; i++) {

            List<Direction> dirs = Arrays.stream(Direction.values())
                    .filter(Direction::isPlanar)
                    .collect(Collectors.toCollection(ArrayList::new));

            Collections.shuffle(dirs);

            boolean placed = false;

            for (Direction dir : dirs) {
                int nx = current.getX() + dir.dx;
                int ny = current.getY() + dir.dy;

                if (map.containsKey(key(nx, ny)))
                    continue;

                Room next = RoomRepository.createRandomRoom(nx, ny);

                current.setExit(dir, next);
                next.setExit(dir.opposite(), current);

                map.put(key(nx, ny), next);

                current = next;
                placed = true;
                break;
            }

            if (!placed) {
                current = new ArrayList<>(map.values())
                        .get(random.nextInt(map.size()));
                i--;
            }
        }

        return map;
    }

    private String key(int x, int y) {
        return x + "," + y;
    }
}

