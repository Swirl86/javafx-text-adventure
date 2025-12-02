package com.sus.questbound.util;

import java.util.List;
import java.util.Random;

public class PlayerMsgHelper {

    private static final Random RANDOM = new Random();

    private static final List<String> DIRECTIONS = List.of(
            "north","south","east","west","up","down"
    );

    public static boolean isDirection(String input) {
        String dir = CommandAliasHelper.normalizeDirection(input);
        return DIRECTIONS.contains(dir);
    }

    public static String randomPlayerMsg(String rawInput) {
        String action = CommandAliasHelper.normalizeAction(rawInput);
        String direction = CommandAliasHelper.normalizeDirection(rawInput);

        if (isDirection(direction)) {
            return switch (direction) {
                case "north" -> randomHint(
                        "You try walking north...",
                        "You step cautiously northward.",
                        "Heading north, you brace yourself."
                );
                case "south" -> randomHint(
                        "You try walking south...",
                        "You move south, keeping your eyes open.",
                        "Stepping south, you listen carefully."
                );
                case "east" -> randomHint(
                        "You try walking east...",
                        "You stride east with determination.",
                        "Moving east, you feel a slight breeze."
                );
                case "west" -> randomHint(
                        "You try walking west...",
                        "You cautiously head west.",
                        "Heading west, you watch your step."
                );
                case "up" -> randomHint(
                        "You look upwards and move up...",
                        "Climbing up, you hope the ceiling is clear.",
                        "Ascending, you carefully check above."
                );
                case "down" -> randomHint(
                        "You look downwards and move down...",
                        "You descend carefully, watching your footing.",
                        "Going down, you stay alert."
                );
                default     -> rawInput;
            };
        }

        return switch (action) {
            case "look" -> randomHint(
                    "You start to look around.",
                    "You glance around carefully.",
                    "You inspect your surroundings.",
                    "You take a moment to study the area.",
                    "Looking closely, you try to notice every detail.",
                    "You peek around, curious what you might find."
            );
            case "inventory" -> randomHint(
                    "You check your belongings.",
                    "Opening your inventory.",
                    "You look through what you are carrying.",
                    "You inspect your items to see what you have.",
                    "You fumble through your inventory, hoping for something useful.",
                    "You review your gear, checking each item carefully."
            );
            case "go" -> randomHint(
                    "You attempt to go " + rawInput + "...",
                    "You boldly stride " + rawInput + ".",
                    "You carefully make your way " + rawInput + ".",
                    "You move " + rawInput + ", hoping nothing jumps out!",
                    "Stepping " + rawInput + ", you brace yourself for surprises."
            );
            case "hint" -> randomHint(
                    "You attempt to get a hint from the GM.",
                    "You call upon the GM for guidance.",
                    "Summoning your inner courage, you ask the GM for a clue.",
                    "You whisper to the GM, hoping for a useful hint.",
                    "Looking around, you silently hope the GM offers some wisdom."
            );
            default -> rawInput;
        };
    }

    public static String randomHint(String... options) {
        return options[RANDOM.nextInt(options.length)];
    }
}
