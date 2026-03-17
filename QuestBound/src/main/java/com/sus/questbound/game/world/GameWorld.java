package com.sus.questbound.game.world;

import com.sus.questbound.game.model.Room;

import java.util.*;

/**
 * Represents the complete game world starting from an initial room.
 * Collects all reachable rooms and exposes them for navigation and map rendering.
 */
public class GameWorld {
    private final Room startRoom;
    private final Set<Room> allRooms;

    public GameWorld(Room startRoom) {
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
