package com.sus.questbound.game.library.room;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sus.questbound.model.Room;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

public class RoomLibrary {

    private static final Random RANDOM = new Random();
    private static final List<RoomDefinition> DEFINITIONS = load();

    private static List<RoomDefinition> load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = RoomLibrary.class
                    .getResourceAsStream("/world/rooms.json");

            if (is == null) {
                throw new IllegalStateException("rooms.json not found in resources");
            }

            return mapper.readValue(is, new TypeReference<List<RoomDefinition>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load rooms.json", e);
        }
    }

    // ---------- public API ----------
    public static Room createStartRoom() {
        RoomDefinition def = DEFINITIONS.stream()
                .filter(d -> d.getTags() != null && d.getTags().contains("start"))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("No room with tag 'start' found"));

        return createRoom(def);
    }

    public static Room createRandomRoom() {
        RoomDefinition def = DEFINITIONS.get(RANDOM.nextInt(DEFINITIONS.size()));
        return createRoom(def);
    }

    // ---------- internal ----------
    private static Room createRoom(RoomDefinition def) {
        String name = random(def.getNames());
        String description = random(def.getDescriptions());
        return new Room(name, description);
    }

    private static String random(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}