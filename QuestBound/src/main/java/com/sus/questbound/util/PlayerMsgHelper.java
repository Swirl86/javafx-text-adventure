package com.sus.questbound.util;

import com.sus.questbound.game.library.player.PlayerMessageLibrary;
import com.sus.questbound.game.library.player.PlayerMessageDefinition;
import com.sus.questbound.model.Action;
import com.sus.questbound.model.Direction;

import java.util.List;
import java.util.Random;

public final class PlayerMsgHelper {

    private static final Random RANDOM = new Random();
    private static final PlayerMessageDefinition DEF = PlayerMessageLibrary.get();

    private PlayerMsgHelper() {}

    public static String getPlayerMsg(Action action, Direction direction) {
        if (action == Action.GO && direction != null) {
            return randomFrom(directionMessages(direction));
        }

        return randomFrom(actionMessages(action));
    }

    public static String getDefaultMessage() {
        return randomFrom(DEF.defaultMessages);
    }

    /*   Internal helpers   */
    private static List<String> directionMessages(Direction direction) {
        return switch (direction) {
            case NORTH -> DEF.directions.north;
            case SOUTH -> DEF.directions.south;
            case EAST  -> DEF.directions.east;
            case WEST  -> DEF.directions.west;
            case UP    -> DEF.directions.up;
            case DOWN  -> DEF.directions.down;
            default    -> DEF.defaultMessages;
        };
    }

    private static List<String> actionMessages(Action action) {
        return switch (action) {
            case LOOK      -> DEF.look;
            case INVENTORY -> DEF.inventory;
            case GO        -> DEF.go;
            case HINT      -> DEF.hint;
            case PICKUP    -> DEF.pickup;
            case DROP      -> DEF.drop;
            default        -> DEF.defaultMessages;
        };
    }

    private static String randomFrom(List<String> options) {
        return options.get(RANDOM.nextInt(options.size()));
    }
}