package com.sus.questbound.game.world;

import com.sus.questbound.game.library.ItemLibrary;
import com.sus.questbound.game.library.RoomLibrary;
import com.sus.questbound.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomWorldGenerator implements WorldGenerator {

    private static final Random RANDOM = new Random();
    private static final int ROOM_COUNT = 8;

    @Override
    public World generate() {

        List<Room> rooms = createRooms();

        connectRoomsRandomly(rooms);
        placeItemsRandomly(rooms);

        Room startRoom = rooms.get(0);
        return new World(startRoom);
    }

    // ---------- rooms ----------

    private List<Room> createRooms() {
        List<Room> rooms = new ArrayList<>();

        // explicit start room
        Room startRoom = RoomLibrary.createStartRoom();
        rooms.add(startRoom);

        // remaining rooms
        for (int i = 1; i < ROOM_COUNT; i++) {
            rooms.add(RoomLibrary.createRandomRoom());
        }

        // shuffle but keep start room at index 0
        Collections.shuffle(rooms.subList(1, rooms.size()));

        return rooms;
    }

    // ---------- exits ----------
    private void connectRoomsRandomly(List<Room> rooms) {
        List<String> directions = List.of("north", "south", "east", "west");

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room current = rooms.get(i);
            Room next = rooms.get(i + 1);

            String dir = randomFreeDirection(current, directions);
            String opposite = opposite(dir);

            current.setExit(dir, next);
            next.setExit(opposite, current);
        }
    }

    private String randomFreeDirection(Room room, List<String> dirs) {
        List<String> free = dirs.stream()
                .filter(d -> room.getExit(d) == null)
                .toList();

        return free.get(RANDOM.nextInt(free.size()));
    }

    private String opposite(String dir) {
        return switch (dir) {
            case "north" -> "south";
            case "south" -> "north";
            case "east" -> "west";
            case "west" -> "east";
            default -> throw new IllegalArgumentException(dir);
        };
    }

    // ---------- items ----------
    private void placeItemsRandomly(List<Room> rooms) {

        int itemCount = RANDOM.nextInt(rooms.size()) + 3;

        for (int i = 0; i < itemCount; i++) {
            Room room = rooms.get(RANDOM.nextInt(rooms.size()));
            room.addItem(ItemLibrary.createRandomItem());
        }
    }
}