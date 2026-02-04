package com.sus.questbound.util;

import com.sus.questbound.game.library.gm.GMMessageLibrary;
import com.sus.questbound.game.library.gm.GMMessageDefinition;
import com.sus.questbound.model.Item;

import java.util.List;
import java.util.Random;

public class GMMsgHelper {

    private static final GMMessageDefinition M = GMMessageLibrary.get();
    private static final Random RANDOM = new Random();

    private static String randomFrom(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    private static String withAttitude(String msg) {
        String attitude = randomFrom(M.attitudes);
        return attitude + ": " + msg;
    }

    public static String unknownCommand() {
        return withAttitude(randomFrom(M.unknownCommand));
    }

    public static String deadEnd(String exits) {
        String msg = randomFrom(M.deadEnd.generic)
                .replace("{exits}", exits);
        return withAttitude(msg);
    }

    public static String deadEndDirectional(String direction) {
        return withAttitude(
                M.deadEnd.directional.replace("{direction}", direction)
        );
    }

    public static String hintAttempt() {
        return withAttitude(randomFrom(M.hintAttempt));
    }

    public static String pickup(Item item) {
        if (item == null) {
            return withAttitude(randomFrom(M.pickup.nothing));
        }

        return withAttitude(
                randomFrom(M.pickup.success)
                        .replace("{item}", item.name())
        );
    }

    public static String drop(Item item) {
        if (item == null) {
            return withAttitude(randomFrom(M.drop.nothing));
        }

        return withAttitude(
                randomFrom(M.drop.success)
                        .replace("{item}", item.name())
        );
    }

    public static String dungeonExitWithKey() {
        return withAttitude(randomFrom(M.dungeonExit.withKey));
    }

    public static String dungeonExitWithoutKey() {
        return withAttitude(randomFrom(M.dungeonExit.withoutKey));
    }

    public static String finalKeyPresenceHint() {
        return withAttitude(randomFrom(M.dungeonExit.keyPresenceHint));
    }
}
