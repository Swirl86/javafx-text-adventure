package com.sus.questbound.game;

public class CommandParser {

    public static Command parse(String input) {
        if (input == null || input.isBlank()) {
            return new Command("", "");
        }

        String[] parts = input.trim().toLowerCase().split("\\s+", 2);

        String action = CommandAliasHelper.normalizeAction(parts[0]);
        String argument = parts.length > 1 ? parts[1] : "";

        return new Command(action, argument);
    }
}
