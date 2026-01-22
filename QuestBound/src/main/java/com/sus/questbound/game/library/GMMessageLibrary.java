package com.sus.questbound.game.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

public class GMMessageLibrary {

    private static final GMMessageDefinition DEF = load();

    private static GMMessageDefinition load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = GMMessageLibrary.class
                    .getResourceAsStream("/messages/gm_messages.json");

            if (is == null) {
                throw new IllegalStateException("gm_messages.json not found");
            }

            return mapper.readValue(is, GMMessageDefinition.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load GM messages", e);
        }
    }

    public static GMMessageDefinition get() {
        return DEF;
    }
}
