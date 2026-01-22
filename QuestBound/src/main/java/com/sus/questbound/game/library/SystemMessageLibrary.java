package com.sus.questbound.game.library;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class SystemMessageLibrary {

    private static final SystemMessageDefinition DEF = load();

    private static SystemMessageDefinition load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = SystemMessageLibrary.class
                    .getResourceAsStream("/messages/system_messages.json");

            if (is == null) {
                throw new IllegalStateException("system_messages.json not found");
            }

            return mapper.readValue(is, SystemMessageDefinition.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load system messages", e);
        }
    }

    public static SystemMessageDefinition get() {
        return DEF;
    }
}

