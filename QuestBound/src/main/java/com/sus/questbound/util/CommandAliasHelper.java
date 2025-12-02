package com.sus.questbound.util;

import java.util.HashMap;
import java.util.Map;

public class CommandAliasHelper {

    private static final Map<String, String> ACTION_ALIASES = new HashMap<>();
    private static final Map<String, String> DIRECTION_ALIASES = new HashMap<>();

    static {
        // === ACTION ALIASES ===
        ACTION_ALIASES.put("look", "look");
        ACTION_ALIASES.put("l", "look");

        ACTION_ALIASES.put("inventory", "inventory");
        ACTION_ALIASES.put("inv", "inventory");
        ACTION_ALIASES.put("i", "inventory");

        ACTION_ALIASES.put("go", "go");
        ACTION_ALIASES.put("g", "go");
        ACTION_ALIASES.put("move", "go");

        // === DIRECTION ALIASES ===
        DIRECTION_ALIASES.put("north", "north");
        DIRECTION_ALIASES.put("n", "north");

        DIRECTION_ALIASES.put("south", "south");
        DIRECTION_ALIASES.put("s", "south");

        DIRECTION_ALIASES.put("east", "east");
        DIRECTION_ALIASES.put("e", "east");

        DIRECTION_ALIASES.put("west", "west");
        DIRECTION_ALIASES.put("w", "west");

        DIRECTION_ALIASES.put("up", "up");
        DIRECTION_ALIASES.put("u", "up");

        DIRECTION_ALIASES.put("down", "down");
        DIRECTION_ALIASES.put("d", "down");
    }

    /** Converts a player-entered action into a standard form.
     *  For example: "l" or "look" → "look", "i" or "inventory" → "inventory", etc.
     */
    public static String normalizeAction(String action) {
        return ACTION_ALIASES.getOrDefault(action, action);
    }

    /** Converts a player-entered direction to a standard form.
     *  For example: "n" or "north" → "north", "s" or "south" → "south", etc.
     */
    public static String normalizeDirection(String dir) {
        return DIRECTION_ALIASES.getOrDefault(dir, dir);
    }
}
