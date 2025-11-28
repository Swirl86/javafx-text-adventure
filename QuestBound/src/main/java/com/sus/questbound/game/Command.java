package com.sus.questbound.game;

public record Command(String action, String argument) {
    public Command {
        if (action == null) action = "";
        if (argument == null) argument = "";
    }
}
