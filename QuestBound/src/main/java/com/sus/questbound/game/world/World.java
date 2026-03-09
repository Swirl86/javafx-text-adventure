package com.sus.questbound.game.world;

import com.sus.questbound.model.Room;

import java.util.*;

public class World {
    private final Room startRoom;
    private final Set<Room> allRooms;

    public World(Room startRoom) {
        this.startRoom = startRoom;
        this.allRooms = collectAllRooms(startRoom);
    }

    private Set<Room> collectAllRooms(Room start) {
        Set<Room> visited = new HashSet<>();
        Queue<Room> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Room room = queue.poll();
            if (!visited.add(room)) continue;
            queue.addAll(room.getExits().values());
        }

        return visited;
    }

    public Room getStartRoom() {
        return startRoom;
    }

    public Set<Room> getAllRooms() {
        return Collections.unmodifiableSet(allRooms);
    }
}
