package com.sus.questbound.game.library.player;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class PlayerMessageLibrary {

    private static final PlayerMessageDefinition DEF = load();

    private static PlayerMessageDefinition load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = PlayerMessageLibrary.class
                    .getResourceAsStream("/messages/player_messages.json");

            if (is == null) {
                throw new IllegalStateException("player_messages.json not found in /messages/");
            }

            return mapper.readValue(is, PlayerMessageDefinition.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load player messages", e);
        }
    }

    public static PlayerMessageDefinition get() {
        return DEF;
    }
}