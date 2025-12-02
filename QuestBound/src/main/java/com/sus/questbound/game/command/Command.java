package com.sus.questbound.game.command;

public record Command(String action, String argument) {
    public Command {
        if (action == null) action = "";
        if (argument == null) argument = "";
    }
}
