package com.sus.questbound.game.library.room;

import java.util.List;

public class RoomDefinition {

    private String type;
    private List<String> names;
    private List<String> descriptions;
    private List<String> tags;

    public String getType() {
        return type;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getDescriptions() {
        return descriptions;
    }

    public List<String> getTags() {
        return tags;
    }
}
