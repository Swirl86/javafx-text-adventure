package com.sus.questbound.util;

import com.sus.questbound.game.library.player.PlayerMessageLibrary;
import com.sus.questbound.game.library.player.PlayerMessageDefinition;

import java.util.List;
import java.util.Random;

public class PlayerMsgHelper {

    private static final Random RANDOM = new Random();

    private static final PlayerMessageDefinition DEF = PlayerMessageLibrary.get();

    private static final List<String> DIRECTIONS = List.of(
            "north","south","east","west","up","down"
    );

    /**
     * Returns a random message for a direction (north, south, east, west, up, down)
     */
    private static String getDirectionMessage(String direction) {
        List<String> msgs = switch (direction) {
            case "north" -> DEF.directions.north;
            case "south" -> DEF.directions.south;
            case "east"  -> DEF.directions.east;
            case "west"  -> DEF.directions.west;
            case "up"    -> DEF.directions.up;
            case "down"  -> DEF.directions.down;
            default      -> DEF.defaultMessages;
        };
        return randomHint(msgs);
    }

    /**
     * Returns a random message for an action (look, inventory, go, hint, pickup, drop)
     */
    private static String getActionMessage(String action) {
        List<String> msgs = switch (action) {
            case "look"      -> DEF.look;
            case "inventory" -> DEF.inventory;
            case "go"        -> DEF.go;
            case "hint"      -> DEF.hint;
            case "pickup"    -> DEF.pickup;
            case "drop"      -> DEF.drop;
            default          -> DEF.defaultMessages;
        };
        return randomHint(msgs);
    }

    private static String randomHint(List<String> options) {
        return options.get(RANDOM.nextInt(options.size()));
    }

    public static boolean isDirection(String input) {
        String dir = CommandAliasHelper.normalizeDirection(input);
        return DIRECTIONS.contains(dir);
    }

    public static String getPlayerMsg(String rawInput) {
        String action = CommandAliasHelper.normalizeAction(rawInput);
        String direction = CommandAliasHelper.normalizeDirection(rawInput);

        if (isDirection(direction)) {
            return getDirectionMessage(direction);
        }

        return getActionMessage(action);
    }

    public static String getDefaultMessage() {
        var defaultMsgs = DEF.defaultMessages;
        return defaultMsgs.get(RANDOM.nextInt(defaultMsgs.size()));
    }
}
